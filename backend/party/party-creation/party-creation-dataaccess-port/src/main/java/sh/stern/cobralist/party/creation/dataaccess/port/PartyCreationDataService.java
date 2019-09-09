package sh.stern.cobralist.party.creation.dataaccess.port;

import sh.stern.cobralist.party.creation.domain.PartyDTO;
import sh.stern.cobralist.party.creation.domain.PlaylistDTO;

public interface PartyCreationDataService {
    PartyDTO getParty(String partyCode);

    PartyDTO createParty(Long userId, String partyName, String password, boolean downVoting, String description, String partyCode);

    void savePlaylistWithTracks(String partyCode, PlaylistDTO playlist);
}
