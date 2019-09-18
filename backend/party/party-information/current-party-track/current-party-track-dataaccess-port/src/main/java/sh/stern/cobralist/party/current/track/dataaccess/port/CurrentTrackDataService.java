package sh.stern.cobralist.party.current.track.dataaccess.port;

import sh.stern.cobralist.party.current.track.domain.ActivePartyDTO;

import java.util.List;

public interface CurrentTrackDataService {
    List<ActivePartyDTO> getActiveParties();

    void changePartyActiveStatus(String partyCode, Boolean activeStatus);

    boolean hasMusicRequestStatusPlayed(String partyCode, String trackId);

    void changeMusicRequestPlayedStatus(String partyCode, String trackId, boolean isPlayedStatus);

    String getPlaylistStreamingId(String partyCode);
}
