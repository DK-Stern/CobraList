package sh.stern.cobralist.party.join.dataaccess.port;

import sh.stern.cobralist.party.join.domain.FindPartyDTO;
import sh.stern.cobralist.party.join.domain.JoinPartyDTO;

public interface JoinPartyDataService {
    GuestCreatedDTO createGuest(JoinPartyDTO joinPartyDto);

    FindPartyDTO findParty(String partyCode);

    Long countGuestName(String guestName, String partyCode);

    String getPartyPassword(String partyCode);
}
