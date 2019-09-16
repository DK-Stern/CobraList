package sh.stern.cobralist.party.creation.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import sh.stern.cobralist.party.creation.api.PartyCreationRequestDTO;
import sh.stern.cobralist.party.creation.api.PartyCreationService;
import sh.stern.cobralist.party.information.domain.PartyInformationDTO;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PartyCreationControllerTest {

    private PartyCreationController testSubject;

    @Mock
    private PartyCreationService partyServiceMock;

    @Before
    public void setUp() {
        testSubject = new PartyCreationController(partyServiceMock);
    }

    @Test
    public void createParty() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final long userId = 1L;
        userPrincipal.setId(userId);
        final String email = "email@email.de";
        userPrincipal.setEmail(email);
        final PartyCreationRequestDTO partyRequest = new PartyCreationRequestDTO();

        final PartyInformationDTO expectedDTO = new PartyInformationDTO();
        when(partyServiceMock.createParty(userPrincipal, partyRequest)).thenReturn(expectedDTO);

        // when
        final ResponseEntity<PartyInformationDTO> resultedResponse = testSubject.createParty(userPrincipal, partyRequest);

        // then
        assertThat(resultedResponse.getBody()).isEqualTo(expectedDTO);
    }
}