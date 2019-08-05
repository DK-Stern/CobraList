package sh.stern.cobralist.party.persistence.exceptions;

import java.text.MessageFormat;

public class PartyNotFoundException extends RuntimeException {

    private static final String PARTY_ERROR_MESSAGE = "Party mit dem Code ''{0}'' konnte nicht gefunden werden.";

    public PartyNotFoundException(String partyId) {
        super(MessageFormat.format(PARTY_ERROR_MESSAGE, partyId));
    }
}
