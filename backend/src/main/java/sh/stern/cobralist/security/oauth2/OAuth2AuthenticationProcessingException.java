package sh.stern.cobralist.security.oauth2;

import org.springframework.security.core.AuthenticationException;

public class OAuth2AuthenticationProcessingException extends AuthenticationException {
    public OAuth2AuthenticationProcessingException(String explanation) {
        super(explanation);
    }
}
