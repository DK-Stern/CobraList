package sh.stern.cobralist.party.delete.api;

import sh.stern.cobralist.user.userprincipal.UserPrincipal;

public interface DeletePartyService {
    void deleteParty(UserPrincipal userPrincipal, String partyCode);
}
