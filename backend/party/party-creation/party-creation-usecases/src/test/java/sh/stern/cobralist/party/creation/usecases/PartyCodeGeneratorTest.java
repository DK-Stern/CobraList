package sh.stern.cobralist.party.creation.usecases;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PartyCodeGeneratorTest {

    private PartyCodeGenerator testSubject;

    @Before
    public void setUp() {
        testSubject = new PartyCodeGenerator();
    }

    @Test
    public void generatedCodeHasSizeSix() {
        // when
        final String resultedPartyCode = testSubject.generatePartyCode();

        // then
        assertThat(resultedPartyCode).hasSize(6);
    }

    @Test
    public void generatedCodeContainsLetters() {
        // given
        final String containsLettersRegex = "\\d*[A-Z]+(\\d|[A-Z])*";

        // when
        final String resultedPartyCode = testSubject.generatePartyCode();

        // then
        assertThat(resultedPartyCode).matches(containsLettersRegex);
    }

    @Test
    public void generatedCodeContainsDigits() {
        // given
        final String containsDigitsRegex = "[A-Z]*\\d+([A-Z]|\\d)*";

        // when
        final String resultedPartyCode = testSubject.generatePartyCode();

        // then
        assertThat(resultedPartyCode).matches(containsDigitsRegex);
    }

    @Test
    public void generatedCodeMatchesConditionsRegex() {
        // given
        final String conditionRegex = "[A-Z]+\\d+(\\d|[A-Z])*|\\d+[A-Z]+(\\d|[A-Z])*";

        // when
        final String resultedPartyCode = testSubject.generatePartyCode();

        // then
        assertThat(resultedPartyCode).matches(conditionRegex);
    }
}