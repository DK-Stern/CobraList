package sh.stern.cobralist.party.join.api;

public class PartyPasswordWrongException extends RuntimeException {
    public PartyPasswordWrongException(String message) {
        super(message);
    }
}
