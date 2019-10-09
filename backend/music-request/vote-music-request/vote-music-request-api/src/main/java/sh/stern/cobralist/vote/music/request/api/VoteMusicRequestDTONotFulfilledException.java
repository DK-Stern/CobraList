package sh.stern.cobralist.vote.music.request.api;

import java.text.MessageFormat;

public class VoteMusicRequestDTONotFulfilledException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Request unvollstaendig. Attribut ''{0}'' ist leer.";

    public VoteMusicRequestDTONotFulfilledException(String attribute) {
        super(MessageFormat.format(ERROR_MESSAGE, attribute));
    }
}
