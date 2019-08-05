package sh.stern.cobralist.party.crud.adapter;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PartyCodeGenerator {

    private static final int CODE_LENGTH = 6;
    private static final char[] CODE_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
    private static final String CODE_REGEX = "[A-Z]+\\d+(\\d|[A-Z])*|\\d+[A-Z]+(\\d|[A-Z])*";

    public String generatePartyCode() {
        final StringBuilder partyCodeBuilder = new StringBuilder();
        final Random random = new Random();
        for (int i = 0; i < CODE_LENGTH; i++) {
            final int nextIndex = random.nextInt(CODE_SET.length);
            partyCodeBuilder.append(CODE_SET[nextIndex]);
        }

        String partyCode = partyCodeBuilder.toString();
        return partyCode.matches(CODE_REGEX) ? partyCode : generatePartyCode();
    }

}
