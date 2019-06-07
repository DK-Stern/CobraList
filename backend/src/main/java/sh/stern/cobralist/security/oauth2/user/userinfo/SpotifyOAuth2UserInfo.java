package sh.stern.cobralist.security.oauth2.user.userinfo;

import java.util.Map;

public class SpotifyOAuth2UserInfo extends OAuth2UserInfo {

    public SpotifyOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getName() {
        return (String) attributes.get("display_name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
}
