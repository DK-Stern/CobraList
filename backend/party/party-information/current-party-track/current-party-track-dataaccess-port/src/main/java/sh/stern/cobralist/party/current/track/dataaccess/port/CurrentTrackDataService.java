package sh.stern.cobralist.party.current.track.dataaccess.port;

import sh.stern.cobralist.party.current.track.domain.ActivePartyDTO;

import java.util.List;

public interface CurrentTrackDataService {
    List<ActivePartyDTO> getActiveParties();

    void changePartyActiveStatus(String partyCode, Boolean activeStatus);

    boolean hasMusicRequestStatusPlayed(Long musicRequestId);

    void changeMusicRequestPlayedStatus(Long musicRequestId, boolean isPlayedStatus);

    String getPlaylistStreamingId(String partyCode);

    Long getMusicRequestId(Long playlistId, String trackStreamingId);

    Long getPlaylistId(String partyCode);
}
