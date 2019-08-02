package sh.stern.cobralist.party.join.dataaccess.port;

import sh.stern.cobralist.party.join.api.FindPartyDTO;
import sh.stern.cobralist.party.join.api.JoinPartyDTO;

public interface JoinPartyDataService {
    GuestCreatedDTO createGuest(JoinPartyDTO joinPartyDto);

    FindPartyDTO findParty(Long partyId);

    Long countGuestName(String guestName);

    String getPartyPassword(Long partyId);
}
