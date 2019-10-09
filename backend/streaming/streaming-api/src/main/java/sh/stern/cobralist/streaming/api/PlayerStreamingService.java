package sh.stern.cobralist.streaming.api;

public interface PlayerStreamingService {

    void startPlaylist(String username, String playlistStreamingId);

    void stopPlaylist(String username);

    void skipSong(String username);

}
