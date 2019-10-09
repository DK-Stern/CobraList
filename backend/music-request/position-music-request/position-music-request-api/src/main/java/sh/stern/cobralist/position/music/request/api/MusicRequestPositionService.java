package sh.stern.cobralist.position.music.request.api;

import sh.stern.cobralist.party.creation.domain.TrackDTO;

public interface MusicRequestPositionService {
    int calculateMusicRequestPosition(Long playlistId, Long musicRequestId, int rating, boolean isDownVote);

    int getPositionOfLastMusicRequest(Long playlistId);

    Long getPlaylistId(String partyCode);

    void persistNewMusicRequest(Long playlistId, TrackDTO trackDTO, int position);

    int decreaseMusicRequestPositions(Long playlistId, int position);

    void updateMusicRequestPosition(Long musicRequestid, Long playlistId, int oldPosition, int newPosition);

    boolean isPlaylistEmpty(Long playlistId);
}
