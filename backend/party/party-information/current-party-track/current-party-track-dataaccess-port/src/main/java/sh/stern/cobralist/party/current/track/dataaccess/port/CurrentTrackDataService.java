package sh.stern.cobralist.party.current.track.dataaccess.port;

import sh.stern.cobralist.party.current.track.domain.ActivePartyDTO;

import java.util.List;
import java.util.Optional;

public interface CurrentTrackDataService {
    List<ActivePartyDTO> getActiveParties();

    void changePartyActiveStatus(String partyCode, Boolean activeStatus);

    boolean hasMusicRequestStatusPlayed(Long musicRequestId);

    void changeMusicRequestPlayedStatus(Long musicRequestId, boolean isPlayedStatus);

    String getPlaylistStreamingId(String partyCode);

    Optional<Long> getMusicRequestIdOfUnplayedTrack(Long playlistId, String trackStreamingId);

    Long getPlaylistId(String partyCode);
}
