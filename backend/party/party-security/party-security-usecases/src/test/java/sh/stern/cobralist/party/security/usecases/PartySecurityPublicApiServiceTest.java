package sh.stern.cobralist.party.security.usecases;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import sh.stern.cobralist.party.security.api.NoPartyParticipantException;
import sh.stern.cobralist.party.security.dataaccess.port.PartySecurityDataService;
import sh.stern.cobralist.user.domain.UserRole;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("squid:S1192") // duplicate literals in tests are ok
@RunWith(MockitoJUnitRunner.class)
public class PartySecurityPublicApiServiceTest {

    private PartySecurityPublicApiService testSubject;

    @Mock
    private PartySecurityDataService partySecurityDataServiceMock;

    @Before
    public void setUp() {
        testSubject = new PartySecurityPublicApiService(partySecurityDataServiceMock);
    }

    @Test
    public void throwsNoExceptionIfGuestIsPartyParticipant() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final long guestId = 1L;
        userPrincipal.setId(guestId);
        userPrincipal.getAuthorities().add(new SimpleGrantedAuthority(UserRole.ROLE_GUEST.name()));
        final String partyCode = "partyCode";

        when(partySecurityDataServiceMock.isGuestPartyParticipant(guestId, partyCode)).thenReturn(true);

        // when u. then
        assertThatCode(() -> testSubject.checkGetPartyInformationPermission(userPrincipal, partyCode))
                .doesNotThrowAnyException();
    }

    @Test
    public void throwsExceptionIfGuestIsNoPartyParticipant() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final long guestId = 1L;
        userPrincipal.setId(guestId);
        userPrincipal.getAuthorities().add(new SimpleGrantedAuthority(UserRole.ROLE_GUEST.name()));
        final String partyCode = "partyCode";

        when(partySecurityDataServiceMock.isGuestPartyParticipant(guestId, partyCode)).thenReturn(false);

        // when u. then
        assertThatExceptionOfType(NoPartyParticipantException.class)
                .isThrownBy(() -> testSubject.checkGetPartyInformationPermission(userPrincipal, partyCode))
                .withMessage("Gast ist der Party nicht zugehörig.");
    }

    @Test
    public void throwsNoExceptionIfUserIsPartyParticipant() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final long userId = 2L;
        userPrincipal.setId(userId);
        userPrincipal.getAuthorities().add(new SimpleGrantedAuthority(UserRole.ROLE_USER.name()));
        final String partyCode = "partyCode";

        when(partySecurityDataServiceMock.isUserPartyParticipant(userId, partyCode)).thenReturn(true);

        // when
        assertThatCode(() -> testSubject.checkGetPartyInformationPermission(userPrincipal, partyCode))
                .doesNotThrowAnyException();
    }

    @Test
    public void throwsExceptionIfUserIsNoPartyParticipant() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final long guestId = 1L;
        userPrincipal.setId(guestId);
        userPrincipal.getAuthorities().add(new SimpleGrantedAuthority(UserRole.ROLE_USER.name()));
        final String partyCode = "partyCode";

        when(partySecurityDataServiceMock.isUserPartyParticipant(guestId, partyCode)).thenReturn(false);

        // when u. then
        assertThatExceptionOfType(NoPartyParticipantException.class)
                .isThrownBy(() -> testSubject.checkGetPartyInformationPermission(userPrincipal, partyCode))
                .withMessage("Benutzer ist der Party nicht zugehörig.");
    }
}