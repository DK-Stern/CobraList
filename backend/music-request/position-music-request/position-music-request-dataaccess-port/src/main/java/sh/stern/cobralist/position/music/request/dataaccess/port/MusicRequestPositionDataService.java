package sh.stern.cobralist.position.music.request.dataaccess.port;

import sh.stern.cobralist.party.creation.domain.TrackDTO;

public interface MusicRequestPositionDataService {
    int getPositionOfLastMusicRequestWithRating(Long playlistId, int rating);

    Long getPlaylistId(String partyCode);

    int increaseMusicRequestPositions(Long playlistId, int position);

    int decreaseMusicRequestPositions(Long playlistId);

    void saveMusicRequest(Long playlistId, TrackDTO trackDTO, int position);
}
