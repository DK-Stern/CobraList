package sh.stern.cobralist.party.join.api;

import sh.stern.cobralist.party.join.domain.FindPartyDTO;
import sh.stern.cobralist.party.join.domain.JoinPartyDTO;

public interface JoinPartyService {
    PartyJoinedDTO joinParty(JoinPartyDTO joinPartyDto);

    FindPartyDTO findParty(String partyCode);
}
