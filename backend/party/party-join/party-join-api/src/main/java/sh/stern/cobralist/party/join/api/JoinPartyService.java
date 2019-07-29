package sh.stern.cobralist.party.join.api;

public interface JoinPartyService {
    PartyJoinedDTO joinParty(JoinPartyDTO joinPartyDto);

    FindPartyDTO findParty(Long partyId);
}
