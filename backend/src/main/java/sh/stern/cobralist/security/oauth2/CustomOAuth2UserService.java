package sh.stern.cobralist.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sh.stern.cobralist.security.UserRole;
import sh.stern.cobralist.security.oauth2.user.UserPrincipalBuilder;
import sh.stern.cobralist.security.oauth2.user.model.AuthProvider;
import sh.stern.cobralist.security.oauth2.user.model.User;
import sh.stern.cobralist.security.oauth2.user.model.UserFactory;
import sh.stern.cobralist.security.oauth2.user.repository.UserRepository;
import sh.stern.cobralist.security.oauth2.user.userinfo.OAuth2UserInfo;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final OAuth2UserInfoFactory userInfoFactory;
    private final UserRepository userRepository;
    private final UserPrincipalBuilder userPrincipalBuilder;
    private final UserFactory userFactory;

    @Autowired
    public CustomOAuth2UserService(OAuth2UserInfoFactory userInfoFactory, UserRepository userRepository,
                                   UserPrincipalBuilder userPrincipalBuilder, UserFactory userFactory) {
        this.userInfoFactory = userInfoFactory;
        this.userRepository = userRepository;
        this.userPrincipalBuilder = userPrincipalBuilder;
        this.userFactory = userFactory;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = getOAuth2User(userRequest);

        OAuth2UserInfo oAuth2UserInfo =
                userInfoFactory.createUserInfo(userRequest.getClientRegistration().getRegistrationId(),
                        oAuth2User.getAttributes());

        checkEmail(oAuth2UserInfo);

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());

        User user = synchronizeUser(userRequest, oAuth2UserInfo, userOptional);

        return userPrincipalBuilder.withId(user.getId())
                .withName(user.getName())
                .withEmail(user.getEmail())
                .withAttributes(oAuth2User.getAttributes())
                .withProvider(user.getProvider())
                .withAuthorities(Collections.singletonList(new SimpleGrantedAuthority(UserRole.ROLE_USER.name())))
                .build();
    }

    private void checkEmail(OAuth2UserInfo oAuth2UserInfo) {
        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from oauth2 provider.");
        }
    }

    private User synchronizeUser(OAuth2UserRequest userRequest, OAuth2UserInfo oAuth2UserInfo,
                                 Optional<User> userOptional) {
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            checkUsedProvider(user, userRequest);
            user = updateExistingUser(user, oAuth2UserInfo);

        } else {
            user = registerNewUser(userRequest, oAuth2UserInfo);
        }
        return user;
    }

    private User registerNewUser(OAuth2UserRequest userRequest, OAuth2UserInfo oAuth2UserInfo) {
        final AuthProvider authProvider = AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId());
        final User user = userFactory.create(oAuth2UserInfo.getName(),
                oAuth2UserInfo.getEmail(),
                authProvider,
                oAuth2UserInfo.getId());

        return userRepository.save(user);
    }

    private User updateExistingUser(User user, OAuth2UserInfo oAuth2UserInfo) {
        user.setName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        return userRepository.save(user);
    }

    private void checkUsedProvider(User user, OAuth2UserRequest userRequest) {
        if (!user.getProvider().toString().equalsIgnoreCase(userRequest.getClientRegistration().getRegistrationId())) {
            throw new OAuth2AuthenticationProcessingException(String.format("Looks like you're signed up with '%s' " +
                    "account. Please use your '%s' account to login.", user.getProvider(), user.getProvider()));
        }
    }

    OAuth2User getOAuth2User(OAuth2UserRequest userRequest) {
        return super.loadUser(userRequest);
    }
}
