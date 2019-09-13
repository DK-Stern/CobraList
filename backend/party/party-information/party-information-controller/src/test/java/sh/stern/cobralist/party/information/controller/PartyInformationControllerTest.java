package sh.stern.cobralist.party.information.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sh.stern.cobralist.party.security.api.PartySecurityService;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PartyInformationControllerTest {

    private PartyInformationController testSubject;

    @Mock
    private PartySecurityService partySecurityServiceMock;

    @Before
    public void setUp() {
        testSubject = new PartyInformationController(partySecurityServiceMock);
    }

    @Test
    public void verifyGetCurrentTrackCallsPartySecurityServiceForPartyParticipantValidation() {
        // given
        final String partyCode = "partyCode";
        final UserPrincipal userPrincipal = new UserPrincipal();

        // when
        testSubject.getCurrentTrack(userPrincipal, partyCode);

        // then
        verify(partySecurityServiceMock).checkGetPartyInformationPermission(userPrincipal, partyCode);
    }
}