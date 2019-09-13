package sh.stern.cobralist.party.security.api;

import sh.stern.cobralist.user.userprincipal.UserPrincipal;

public interface PartySecurityService {
    void checkGetPartyInformationPermission(UserPrincipal userPrincipal, String partyCode);
}
