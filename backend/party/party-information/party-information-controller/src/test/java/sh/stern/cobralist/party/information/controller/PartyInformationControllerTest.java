package sh.stern.cobralist.party.information.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import sh.stern.cobralist.party.current.track.api.CurrentTrackService;
import sh.stern.cobralist.party.current.track.domain.CurrentPlaybackDTO;
import sh.stern.cobralist.party.security.api.PartySecurityService;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PartyInformationControllerTest {

    private PartyInformationController testSubject;

    @Mock
    private PartySecurityService partySecurityServiceMock;

    @Mock
    private CurrentTrackService currentTrackServiceMock;

    @Before
    public void setUp() {
        testSubject = new PartyInformationController(partySecurityServiceMock, currentTrackServiceMock);
    }

    @Test
    public void verifyGetCurrentTrackCallsPartySecurityServiceForPartyParticipantValidation() {
        // given
        final String partyCode = "partyCode";
        final UserPrincipal userPrincipal = new UserPrincipal();

        // when
        testSubject.getPartyInformation(userPrincipal, partyCode);

        // then
        verify(partySecurityServiceMock).checkGetPartyInformationPermission(userPrincipal, partyCode);
    }

    @Test
    public void getCurrentTrackFromParty() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final String partyCode = "partyCode";

        final CurrentPlaybackDTO expectedCurrentPlaybackDTO = new CurrentPlaybackDTO();
        when(currentTrackServiceMock.getCurrentTrack(partyCode)).thenReturn(expectedCurrentPlaybackDTO);

        // when
        final ResponseEntity<PartyInformationDTO> partyInformationResponse = testSubject.getPartyInformation(userPrincipal, partyCode);

        // then
        assertThat(partyInformationResponse.getBody().getCurrentPlaybackDTO()).isEqualTo(expectedCurrentPlaybackDTO);
    }
}