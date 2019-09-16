package sh.stern.cobralist.party.information.api;

import sh.stern.cobralist.party.information.domain.PartyInformationDTO;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

public interface PartyInformationService {
    PartyInformationDTO getPartyInformation(UserPrincipal userPrincipal, String partyCode);
}
