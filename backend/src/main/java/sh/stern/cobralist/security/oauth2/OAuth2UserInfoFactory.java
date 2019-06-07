package sh.stern.cobralist.security.oauth2;

import org.springframework.stereotype.Component;
import sh.stern.cobralist.security.oauth2.user.model.AuthProvider;
import sh.stern.cobralist.security.oauth2.user.userinfo.OAuth2UserInfo;
import sh.stern.cobralist.security.oauth2.user.userinfo.SpotifyOAuth2UserInfo;

import java.util.Map;

@Component
public class OAuth2UserInfoFactory {


    public OAuth2UserInfo createUserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(AuthProvider.spotify.toString())) {
            return new SpotifyOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException(String.format("Sorry!, Login with '%s' is not supported" +
                    " yet.", registrationId));
        }
    }
}
