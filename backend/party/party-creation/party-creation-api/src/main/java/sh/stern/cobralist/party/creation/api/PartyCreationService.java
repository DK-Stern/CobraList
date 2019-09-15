package sh.stern.cobralist.party.creation.api;

public interface PartyCreationService {
    PartyCreationResponseDTO createParty(String username, Long userId, PartyCreationRequestDTO partyDTO);
}
