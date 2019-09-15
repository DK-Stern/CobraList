package sh.stern.cobralist.party.current.track.dataaccess.port;

import sh.stern.cobralist.party.current.track.domain.ActivePartyDTO;

import java.util.List;

public interface CurrentTrackDataService {
    List<ActivePartyDTO> getActiveParties();

    void changePartyActiveStatus(String partyCode, Boolean activeStatus);
}
