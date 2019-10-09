package sh.stern.cobralist.party.security.dataaccess.port;

public interface PartySecurityDataService {
    boolean isUserPartyParticipant(Long userId, String partyCode);

    boolean isGuestPartyParticipant(Long guestId, String partyCode);

    boolean isPartyCreator(Long userId, String partyCode);
}
