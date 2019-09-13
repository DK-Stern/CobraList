package sh.stern.cobralist.party.security.api;

public class NoPartyParticipantException extends RuntimeException {
    public NoPartyParticipantException(String message) {
        super(message);
    }
}
