package sh.stern.cobralist.party.information.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import sh.stern.cobralist.party.information.domain.PartyInformationDTO;
import sh.stern.cobralist.party.information.api.PartyInformationService;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PartyInformationControllerTest {

    private PartyInformationController testSubject;

    @Mock
    private PartyInformationService partyInformationServiceMock;

    @Before
    public void setUp() {
        testSubject = new PartyInformationController(partyInformationServiceMock);
    }

    @Test
    public void getPartyInformation() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final String partyCode = "partyCode";

        final PartyInformationDTO expectedPartyInformationDTO = new PartyInformationDTO();
        when(partyInformationServiceMock.getPartyInformation(userPrincipal, partyCode)).thenReturn(expectedPartyInformationDTO);

        // when
        final ResponseEntity<PartyInformationDTO> partyInformationResponse = testSubject.getPartyInformation(userPrincipal, partyCode);

        // then
        assertThat(partyInformationResponse.getBody()).isEqualTo(expectedPartyInformationDTO);
    }
}