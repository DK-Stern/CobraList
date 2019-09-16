package sh.stern.cobralist.party.creation.api;

import sh.stern.cobralist.party.information.domain.PartyInformationDTO;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

public interface PartyCreationService {
    PartyInformationDTO createParty(UserPrincipal userPrincipal, PartyCreationRequestDTO partyDTO);
}
