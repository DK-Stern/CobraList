package sh.stern.cobralist.add.music.request.api;

import java.text.MessageFormat;

public class AddMusicRequestDTONotFulfilledException extends RuntimeException {
    private static final String ERROR_MESSAGE = "Das Request-Objekt ist nicht vollstaendig. Das Attribute ''{0}'' ist leer.";

    public AddMusicRequestDTONotFulfilledException(String missingAttribute) {
        super(MessageFormat.format(ERROR_MESSAGE, missingAttribute));
    }
}
