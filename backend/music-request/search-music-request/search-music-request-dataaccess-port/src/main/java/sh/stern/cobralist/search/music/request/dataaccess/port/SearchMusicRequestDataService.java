package sh.stern.cobralist.search.music.request.dataaccess.port;

public interface SearchMusicRequestDataService {
    Long getPlaylistId(String partyCode);

    boolean isMusicRequestAlreadyInPlaylist(Long playlistId, String trackId);

    String getPartyCreatorStreamingId(String partyCode);
}
