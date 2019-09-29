package sh.stern.cobralist.party.persistence.exceptions;

public class PlaylistNotFoundException extends RuntimeException {

    private static final String PARTY_ERROR_MESSAGE = "Playlist mit der id '%s' konnte nicht gefunden werden.";

    public PlaylistNotFoundException(Long playlistId) {
        super(String.format(PARTY_ERROR_MESSAGE, playlistId));
    }
}
