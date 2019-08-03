package sh.stern.cobralist.party.crud.api;


import sh.stern.cobralist.party.persistence.domain.Party;

public interface PartyCRUDService {
    // todo: dto statt entitiy zuruekgeben
    Party createParty(PartyCreationDTO partyDTO, Long userId);
}
