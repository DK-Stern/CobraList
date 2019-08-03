package sh.stern.cobralist.security.oauth2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import sh.stern.cobralist.party.persistence.dataaccess.UserRepository;
import sh.stern.cobralist.party.persistence.domain.User;
import sh.stern.cobralist.party.persistence.domain.UserFactory;
import sh.stern.cobralist.user.userinfo.OAuth2UserInfo;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;
import sh.stern.cobralist.user.userprincipal.UserPrincipalBuilder;

import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;
import static sh.stern.cobralist.user.domain.AuthProvider.spotify;

@RunWith(MockitoJUnitRunner.class)
public class CustomOAuth2UserServiceTest {

    private CustomOAuth2UserService testSubjectSpy;

    @Mock
    private OAuth2UserInfoFactory userInfoFactoryMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private OAuth2User oAuth2UserMock;

    @Mock
    private UserPrincipalBuilder userPrincipalBuilderMock;

    @Mock
    private UserFactory userFactoryMock;

    @Before
    public void setUp() {
        testSubjectSpy = spy(new CustomOAuth2UserService(
                userInfoFactoryMock,
                userRepositoryMock,
                userPrincipalBuilderMock,
                userFactoryMock));
        doReturn(oAuth2UserMock).when(testSubjectSpy).getOAuth2User(any());
    }

    @Test
    public void loadExistingUser() {
        // given
        final OAuth2UserRequest userRequestMock = mock(OAuth2UserRequest.class);
        final ClientRegistration clientRegistration = mock(ClientRegistration.class);
        final String registrationId = "spotify";
        when(userRequestMock.getClientRegistration()).thenReturn(clientRegistration);
        when(clientRegistration.getRegistrationId()).thenReturn(registrationId);

        final HashMap<String, Object> attributes = new HashMap<>();
        when(oAuth2UserMock.getAttributes()).thenReturn(attributes);

        final OAuth2UserInfo oAuth2UserInfoMock = mock(OAuth2UserInfo.class);
        final String name = "name";
        final String email = "email@email.de";
        when(oAuth2UserInfoMock.getEmail()).thenReturn(email);
        when(oAuth2UserInfoMock.getName()).thenReturn(name);
        when(userInfoFactoryMock.createUserInfo(registrationId, attributes)).thenReturn(oAuth2UserInfoMock);

        final User user = new User();
        final long userId = 1L;
        user.setId(userId);
        user.setProvider(spotify);
        when(userRepositoryMock.findByEmail(email)).thenReturn(Optional.of(user));

        when(userRepositoryMock.save(user)).thenReturn(user);

        when(userPrincipalBuilderMock.withPartyId(userId)).thenReturn(userPrincipalBuilderMock);
        when(userPrincipalBuilderMock.withName(name)).thenReturn(userPrincipalBuilderMock);
        when(userPrincipalBuilderMock.withEmail(email)).thenReturn(userPrincipalBuilderMock);
        when(userPrincipalBuilderMock.withProvider(spotify)).thenReturn(userPrincipalBuilderMock);
        when(userPrincipalBuilderMock.withAttributes(attributes)).thenReturn(userPrincipalBuilderMock);
        when(userPrincipalBuilderMock.withAuthorities(anyList())).thenReturn(userPrincipalBuilderMock);
        final UserPrincipal expectedUserPrincipalMock = mock(UserPrincipal.class);
        when(userPrincipalBuilderMock.build()).thenReturn(expectedUserPrincipalMock);

        // when
        final OAuth2User resultedOAuth2User = testSubjectSpy.loadUser(userRequestMock);

        // then
        assertThat(resultedOAuth2User).isEqualTo(expectedUserPrincipalMock);
    }

    @Test
    public void throwsOAuth2AuthenticationProcessingExceptionIfRegistrationIdDiffersFromUser() {
        // given
        final OAuth2UserRequest userRequestMock = mock(OAuth2UserRequest.class);
        final ClientRegistration clientRegistration = mock(ClientRegistration.class);
        final String registrationId = "otherProvider";
        when(userRequestMock.getClientRegistration()).thenReturn(clientRegistration);
        when(clientRegistration.getRegistrationId()).thenReturn(registrationId);

        final HashMap<String, Object> attributes = new HashMap<>();
        when(oAuth2UserMock.getAttributes()).thenReturn(attributes);

        final OAuth2UserInfo oAuth2UserInfoMock = mock(OAuth2UserInfo.class);
        final String name = "name";
        final String email = "email@email.de";
        when(oAuth2UserInfoMock.getEmail()).thenReturn(email);
        when(userInfoFactoryMock.createUserInfo(registrationId, attributes)).thenReturn(oAuth2UserInfoMock);

        final User user = new User();
        user.setProvider(spotify);
        when(userRepositoryMock.findByEmail(email)).thenReturn(Optional.of(user));

        // then
        assertThatCode(() -> testSubjectSpy.loadUser(userRequestMock))
                .isInstanceOf(OAuth2AuthenticationProcessingException.class)
                .withFailMessage("Looks like you're signed up with 'otherProvider' " +
                        "account. Please use your 'spotify' account to login.");
    }

    @Test
    public void loadNewUser() {
        // given
        final OAuth2UserRequest userRequestMock = mock(OAuth2UserRequest.class);
        final ClientRegistration clientRegistration = mock(ClientRegistration.class);
        final String registrationId = "spotify";
        when(userRequestMock.getClientRegistration()).thenReturn(clientRegistration);
        when(clientRegistration.getRegistrationId()).thenReturn(registrationId);

        final HashMap<String, Object> attributes = new HashMap<>();
        when(oAuth2UserMock.getAttributes()).thenReturn(attributes);

        final OAuth2UserInfo oAuth2UserInfoMock = mock(OAuth2UserInfo.class);
        final String name = "name";
        final String email = "email@email.de";
        final String providerId = "providerId";
        when(oAuth2UserInfoMock.getName()).thenReturn(name);
        when(oAuth2UserInfoMock.getId()).thenReturn(providerId);
        when(oAuth2UserInfoMock.getEmail()).thenReturn(email);
        when(userInfoFactoryMock.createUserInfo(registrationId, attributes)).thenReturn(oAuth2UserInfoMock);

        when(userRepositoryMock.findByEmail(email)).thenReturn(Optional.empty());

        final User user = new User();
        final long userId = 1L;
        user.setId(userId);
        user.setName(name);
        user.setProvider(spotify);
        user.setEmail(email);
        when(userFactoryMock.create(name, email, spotify, providerId)).thenReturn(user);

        when(userRepositoryMock.save(user)).thenReturn(user);

        when(userPrincipalBuilderMock.withPartyId(userId)).thenReturn(userPrincipalBuilderMock);
        when(userPrincipalBuilderMock.withName(name)).thenReturn(userPrincipalBuilderMock);
        when(userPrincipalBuilderMock.withEmail(email)).thenReturn(userPrincipalBuilderMock);
        when(userPrincipalBuilderMock.withProvider(spotify)).thenReturn(userPrincipalBuilderMock);
        when(userPrincipalBuilderMock.withAttributes(attributes)).thenReturn(userPrincipalBuilderMock);
        when(userPrincipalBuilderMock.withAuthorities(anyList())).thenReturn(userPrincipalBuilderMock);
        final UserPrincipal expectedUserPrincipalMock = mock(UserPrincipal.class);
        when(userPrincipalBuilderMock.build()).thenReturn(expectedUserPrincipalMock);

        // when
        final OAuth2User resultedOAuth2User = testSubjectSpy.loadUser(userRequestMock);

        // then
        assertThat(resultedOAuth2User).isEqualTo(expectedUserPrincipalMock);
    }
}