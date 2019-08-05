package sh.stern.cobralist.party.persistence.exceptions;

import java.text.MessageFormat;

public class UserNotFoundException extends RuntimeException {

    private static final String USER_NOT_FOUND_ERROR_MESSAGE = "Benutzer mit der Id ''{0,number,#}'' konnte nicht gefunden werden.";

    public UserNotFoundException(Long userId) {
        super(MessageFormat.format(USER_NOT_FOUND_ERROR_MESSAGE, userId));
    }
}
