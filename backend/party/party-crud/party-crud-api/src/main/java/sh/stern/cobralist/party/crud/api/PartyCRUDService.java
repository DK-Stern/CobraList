package sh.stern.cobralist.party.crud.api;


public interface PartyCRUDService {
    PartyCreationDTO createParty(PartyCreationDTO partyDTO, Long userId);

    PartyCreationDTO getParty(Long partyId);
}
