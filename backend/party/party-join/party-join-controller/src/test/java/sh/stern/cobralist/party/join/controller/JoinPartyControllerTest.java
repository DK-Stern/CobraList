package sh.stern.cobralist.party.join.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import sh.stern.cobralist.party.join.api.JoinPartyService;
import sh.stern.cobralist.party.join.api.PartyJoinedDTO;
import sh.stern.cobralist.party.join.domain.FindPartyDTO;
import sh.stern.cobralist.party.join.domain.JoinPartyDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JoinPartyControllerTest {

    private JoinPartyController testSubject;

    @Mock
    private JoinPartyService joinPartyServiceMock;

    @Before
    public void setUp() throws Exception {
        testSubject = new JoinPartyController(joinPartyServiceMock);
    }

    @Test
    public void joinParty() {
        // given
        final JoinPartyDTO joinPartyDto = new JoinPartyDTO();

        final PartyJoinedDTO expectedDTO = new PartyJoinedDTO();
        when(joinPartyServiceMock.joinParty(joinPartyDto)).thenReturn(expectedDTO);

        // when
        final ResponseEntity<PartyJoinedDTO> resultedResponse = testSubject.joinParty(joinPartyDto);

        // then
        assertThat(resultedResponse.getBody()).isEqualTo(expectedDTO);
    }

    @Test
    public void findParty() {
        // given
        final String partyCode = "partyCode";

        final FindPartyDTO expectedDTO = new FindPartyDTO();
        when(joinPartyServiceMock.findParty(partyCode)).thenReturn(expectedDTO);

        // when
        final ResponseEntity<FindPartyDTO> resultedResponse = testSubject.findParty(partyCode);

        // then
        assertThat(resultedResponse.getBody()).isEqualTo(expectedDTO);
    }
}