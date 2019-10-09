package sh.stern.cobralist.position.music.request.dataaccess.port;

import sh.stern.cobralist.party.creation.domain.TrackDTO;

import java.util.List;

public interface MusicRequestPositionDataService {
    int getPositionOfLastMusicRequest(Long playlistId);

    List<MusicRequestPositionDTO> getMusicRequestWithSameRatingForUpVote(Long playlistId, int rating);

    List<MusicRequestPositionDTO> getMusicRequestWithSameRatingForDownVote(Long playlistId, int rating);

    int getTopRatingInPlaylist(Long playlistId);

    int getWorstRatingInPlaylist(Long playlistId);

    Long getPlaylistId(String partyCode);

    int incrementMusicRequestPositions(Long playlistId, int position);

    int incrementMusicRequestPositionInterval(Long playlistId, int startPosition, int endPosition);

    int decrementMusicRequestPositions(Long playlistId, int position);

    int decrementMusicRequestPositionInterval(Long playlistId, int startPosition, int endPosition);

    void saveMusicRequest(Long playlistId, TrackDTO trackDTO, int position);

    void updateMusicRequestPosition(Long musicRequestId, int newPosition);

    boolean isPlaylistEmpty(Long playlistId);

    int getUpVotes(Long musicRequestId);
}
