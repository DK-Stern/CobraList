package sh.stern.cobralist.position.music.request.api;

import sh.stern.cobralist.party.creation.domain.TrackDTO;

public interface MusicRequestPositionService {
    int calculateMusicRequestPosition(Long playlistId, int rating);

    Long getPlaylistId(String partyCode);

    void persistNewMusicRequest(Long playlistId, TrackDTO trackDTO, int position);

    int decreaseMusicRequestPositions(Long playlistId);
}
