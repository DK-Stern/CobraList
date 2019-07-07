package sh.stern.cobralist.security.oauth2;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class OAuth2SessionExpiredException extends RuntimeException {

    public OAuth2SessionExpiredException(String message) {
        super(message);
    }
}
