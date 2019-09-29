package sh.stern.cobralist.add.music.request.api;

public class MusicRequestAlreadyExistException extends RuntimeException {
    public MusicRequestAlreadyExistException(String trackId) {
        super("MusicRequest mit streaming id '" + trackId + "' existiert bereits.");
    }
}
