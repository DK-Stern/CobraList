package sh.stern.cobralist.search.musicrequest.dataaccess.port;

public interface SearchMusicRequestDataService {
    Long getPlaylistId(String partyCode);

    boolean isMusicRequestAlreadyInPlaylist(Long playlistId, String trackId);
}
