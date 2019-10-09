package sh.stern.cobralist.party.persistence.exceptions;

public class MusicRequestNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "MusicRequest mit der id '%d' konnte nicht gefunden werden.";

    public MusicRequestNotFoundException() {
    }

    public MusicRequestNotFoundException(Long trackId) {
        super(String.format(ERROR_MESSAGE, trackId));
    }
}
