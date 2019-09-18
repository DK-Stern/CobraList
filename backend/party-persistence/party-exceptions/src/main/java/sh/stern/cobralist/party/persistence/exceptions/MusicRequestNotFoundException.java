package sh.stern.cobralist.party.persistence.exceptions;

public class MusicRequestNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "MusicRequest mit der PlaylistId '%d' und TrackId '%s' konnte nicht gefunden werden.";

    public MusicRequestNotFoundException(Long playlistId, String trackId) {
        super(String.format(ERROR_MESSAGE, playlistId, trackId));
    }
}
