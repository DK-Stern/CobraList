package sh.stern.cobralist.party.joinparty.dataaccess.api;

import sh.stern.cobralist.party.joinparty.api.JoinPartyDTO;

public interface JoinPartyDataService {

    GuestCreatedDTO createGuest(JoinPartyDTO joinPartyDto);
}
