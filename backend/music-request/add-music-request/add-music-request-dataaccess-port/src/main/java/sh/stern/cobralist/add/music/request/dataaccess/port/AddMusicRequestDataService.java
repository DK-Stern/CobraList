package sh.stern.cobralist.add.music.request.dataaccess.port;

public interface AddMusicRequestDataService {
    String getPlaylistStreamingId(Long playlistId);

    boolean doesMusicRequestExist(Long playlistId, String trackStreamingId);
}
