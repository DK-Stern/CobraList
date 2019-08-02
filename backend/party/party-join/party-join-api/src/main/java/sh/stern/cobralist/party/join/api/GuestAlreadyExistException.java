package sh.stern.cobralist.party.join.api;

public class GuestAlreadyExistException extends RuntimeException {
    public GuestAlreadyExistException(String message) {
        super(message);
    }
}
