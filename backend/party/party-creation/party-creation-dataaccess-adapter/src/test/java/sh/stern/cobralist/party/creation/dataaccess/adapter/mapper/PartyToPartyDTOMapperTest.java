package sh.stern.cobralist.party.creation.dataaccess.adapter.mapper;

import org.junit.Before;
import org.junit.Test;
import sh.stern.cobralist.party.creation.domain.PartyDTO;
import sh.stern.cobralist.party.persistence.domain.Party;

import static org.assertj.core.api.Assertions.assertThat;

public class PartyToPartyDTOMapperTest {

    private PartyToPartyDTOMapper testSubject;

    @Before
    public void setUp() throws Exception {
        testSubject = new PartyToPartyDTOMapper();
    }

    @Test
    public void mapPartyCode() {
        // given
        final String expectedPartyCode = "partyCode";

        final Party party = new Party();
        party.setPartyCode(expectedPartyCode);

        // when
        final PartyDTO resultedDTO = testSubject.map(party);

        // then
        assertThat(resultedDTO.getPartyCode()).isEqualTo(expectedPartyCode);
    }

    @Test
    public void mapPartyName() {
        // given
        final String expectedPartyName = "partyCode";

        final Party party = new Party();
        party.setName(expectedPartyName);

        // when
        final PartyDTO resultedDTO = testSubject.map(party);

        // then
        assertThat(resultedDTO.getPartyName()).isEqualTo(expectedPartyName);
    }

    @Test
    public void mapDescription() {
        // given
        final String expectedDescription = "partyCode";

        final Party party = new Party();
        party.setDescription(expectedDescription);

        // when
        final PartyDTO resultedDTO = testSubject.map(party);

        // then
        assertThat(resultedDTO.getDescription()).isEqualTo(expectedDescription);
    }

    @Test
    public void mapDownVoting() {
        // given
        final boolean isDownVotable = true;

        final Party party = new Party();
        party.setDownVotable(isDownVotable);

        // when
        final PartyDTO resultedDTO = testSubject.map(party);

        // then
        assertThat(resultedDTO.isDownVoting()).isEqualTo(isDownVotable);
    }

    @Test
    public void mapPassword() {
        // given
        final String expectedPassword = "password";
        final Party party = new Party();
        party.setPassword(expectedPassword);

        // when
        final PartyDTO resultedDTO = testSubject.map(party);

        // then
        assertThat(resultedDTO.getPassword()).isEqualTo(expectedPassword);
    }
}