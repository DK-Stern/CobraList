package sh.stern.cobralist.party.creation.api;


import sh.stern.cobralist.party.creation.domain.PartyDTO;

public interface PartyCreationService {
    PartyCreationResponseDTO createParty(String username, Long userId, PartyCreationRequestDTO partyDTO);

    PartyDTO getParty(String partyCode);
}
