package sh.stern.cobralist.party.security.api;

public class NoPartyCreatorException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Der Benutzer mit der id '%s' ist nicht der Ersteller der Party mit dem Code '%s' und hat dementsprechend nicht die Berechtigung dies zu tun.";

    public NoPartyCreatorException(Long userId, String partyCode) {
        super(String.format(ERROR_MESSAGE, userId, partyCode));
    }
}
