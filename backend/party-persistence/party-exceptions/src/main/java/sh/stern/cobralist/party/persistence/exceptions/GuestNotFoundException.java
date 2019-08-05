package sh.stern.cobralist.party.persistence.exceptions;

import java.text.MessageFormat;

public class GuestNotFoundException extends RuntimeException {
    private static final String GUEST_ERROR_MESSAGE = "Gast mit der ID ''{0,number,#}'' konnte nicht gefunden werden.";

    public GuestNotFoundException(Long guestId) {
        super(MessageFormat.format(GUEST_ERROR_MESSAGE, guestId));
    }
}
