package sh.stern.cobralist.party.security.dataaccess.adapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sh.stern.cobralist.party.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.party.persistence.domain.Guest;
import sh.stern.cobralist.party.persistence.domain.Party;
import sh.stern.cobralist.party.persistence.domain.User;
import sh.stern.cobralist.party.persistence.exceptions.PartyNotFoundException;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@SuppressWarnings("squid:S1192") // duplicate literals in tests are ok
@RunWith(MockitoJUnitRunner.class)
public class PartySecurityPublicApiDataServiceTest {

    private PartySecurityPublicApiDataService testSubject;

    @Mock
    private PartyRepository partyRepositoryMock;

    @Before
    public void setUp() {
        testSubject = new PartySecurityPublicApiDataService(partyRepositoryMock);
    }

    @Test
    public void userIsPartyParticipant() {
        // given
        final long userId = 1L;
        final String partyCode = "partyCode";

        final User user = new User();
        user.setId(userId);
        final Party party = new Party();
        party.setUser(user);
        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.of(party));

        // when
        final boolean isUserPartyParticipant = testSubject.isUserPartyParticipant(userId, partyCode);

        // then
        assertThat(isUserPartyParticipant).isTrue();
    }

    @Test
    public void userIsNotPartyParticipant() {
        // given
        final long userId = 1L;
        final String partyCode = "partyCode";

        final User user = new User();
        user.setId(2L);
        final Party party = new Party();
        party.setUser(user);
        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.of(party));

        // when
        final boolean isUserPartyParticipant = testSubject.isUserPartyParticipant(userId, partyCode);

        // then
        assertThat(isUserPartyParticipant).isFalse();
    }

    @Test
    public void isUserPartyParticipantThrowsExceptionIfPartyNotFound() {
        // given
        final long userId = 1L;
        final String partyCode = "partyCode";

        // when u. then
        assertThatExceptionOfType(PartyNotFoundException.class)
                .describedAs("Party mit dem Code '" + partyCode + "' konnte nicht gefunden werden.")
                .isThrownBy(() -> testSubject.isUserPartyParticipant(userId, partyCode));
    }

    @Test
    public void guestIsPartyParticipant() {
        // given
        final long guestId = 1L;
        final String partyCode = "partyCode";

        final Party party = new Party();
        final Guest guest = new Guest();
        guest.setId(guestId);
        party.setGuests(Collections.singleton(guest));
        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.of(party));

        // when
        final boolean isPartyParticipant = testSubject.isGuestPartyParticipant(guestId, partyCode);

        // then
        assertThat(isPartyParticipant).isTrue();
    }

    @Test
    public void guestIsNotPartyParticipant() {
        // given
        final long guestId = 1L;
        final String partyCode = "partyCode";

        final Party party = new Party();
        final Guest guest = new Guest();
        guest.setId(2L);
        party.setGuests(Collections.singleton(guest));
        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.of(party));

        // when
        final boolean isPartyParticipant = testSubject.isGuestPartyParticipant(guestId, partyCode);

        // then
        assertThat(isPartyParticipant).isFalse();
    }

    @Test
    public void isGuestPartyParticipantThrowsExceptionIfPartyNotFound() {
        // given
        final long guestId = 1L;
        final String partyCode = "partyCode";

        // when u. then
        assertThatExceptionOfType(PartyNotFoundException.class)
                .describedAs("Party mit dem Code '" + partyCode + "' konnte nicht gefunden werden.")
                .isThrownBy(() -> testSubject.isGuestPartyParticipant(guestId, partyCode));
    }
}