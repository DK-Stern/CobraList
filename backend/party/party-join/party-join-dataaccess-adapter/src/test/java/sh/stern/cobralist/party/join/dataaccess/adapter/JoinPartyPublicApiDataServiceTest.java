package sh.stern.cobralist.party.join.dataaccess.adapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sh.stern.cobralist.party.join.api.FindPartyDTO;
import sh.stern.cobralist.party.join.api.JoinPartyDTO;
import sh.stern.cobralist.party.join.api.PartyNotFoundException;
import sh.stern.cobralist.party.join.dataaccess.port.GuestCreatedDTO;
import sh.stern.cobralist.persistence.dataaccess.GuestRepository;
import sh.stern.cobralist.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.persistence.domain.Guest;
import sh.stern.cobralist.persistence.domain.Party;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JoinPartyPublicApiDataServiceTest {

    private JoinPartyPublicApiDataService testSubject;

    @Mock
    private GuestRepository guestRepositoryMock;

    @Mock
    private PartyRepository partyRepositoryMock;

    @Before
    public void setUp() throws Exception {
        testSubject = new JoinPartyPublicApiDataService(guestRepositoryMock, partyRepositoryMock);
    }

    @Test
    public void createGuestDTOWithName() {
        // given
        final long partyId = 1L;
        final String guestName = "Bob";

        final JoinPartyDTO joinPartyDto = new JoinPartyDTO();
        joinPartyDto.setPartyId(partyId);
        joinPartyDto.setGuestName(guestName);

        final Party party = new Party();
        when(partyRepositoryMock.findById(partyId)).thenReturn(Optional.of(party));

        final Guest savedGuest = new Guest();
        savedGuest.setName(guestName);
        savedGuest.setParty(party);

        when(guestRepositoryMock.saveAndFlush(any(Guest.class))).thenReturn(savedGuest);

        // when
        final GuestCreatedDTO resultedGuestDTO = testSubject.createGuest(joinPartyDto);

        // then
        assertThat(resultedGuestDTO.getName()).isEqualTo(guestName);
    }

    @Test
    public void createGuestDTOWithPartyId() {
        // given
        final long partyId = 1L;
        final String guestName = "Bob";

        final JoinPartyDTO joinPartyDto = new JoinPartyDTO();
        joinPartyDto.setPartyId(partyId);
        joinPartyDto.setGuestName(guestName);

        final Party partyMock = mock(Party.class);
        when(partyMock.getId()).thenReturn(partyId);
        when(partyRepositoryMock.findById(partyId)).thenReturn(Optional.of(partyMock));

        final Guest savedGuest = new Guest();
        savedGuest.setName(guestName);
        savedGuest.setParty(partyMock);

        when(guestRepositoryMock.saveAndFlush(any(Guest.class))).thenReturn(savedGuest);

        // when
        final GuestCreatedDTO resultedGuestDTO = testSubject.createGuest(joinPartyDto);

        // then
        assertThat(resultedGuestDTO.getPartyId()).isEqualTo(partyId);
    }

    @Test
    public void saveGuestOnCreationWithName() {
        // given
        final long partyId = 1L;
        final String guestName = "Bob";

        final JoinPartyDTO joinPartyDto = new JoinPartyDTO();
        joinPartyDto.setPartyId(partyId);
        joinPartyDto.setGuestName(guestName);

        final Party party = new Party();
        when(partyRepositoryMock.findById(partyId)).thenReturn(Optional.of(party));

        final Guest savedGuest = new Guest();
        savedGuest.setName(guestName);
        savedGuest.setParty(party);

        final ArgumentCaptor<Guest> guestArgumentCaptor = ArgumentCaptor.forClass(Guest.class);
        when(guestRepositoryMock.saveAndFlush(guestArgumentCaptor.capture())).thenReturn(savedGuest);

        // when
        testSubject.createGuest(joinPartyDto);

        // then
        final Guest resultedSavedGuest = guestArgumentCaptor.getValue();
        assertThat(resultedSavedGuest.getName()).isEqualTo(guestName);
    }

    @Test
    public void saveGuestOnCreationWithParty() {
        // given
        final long partyId = 1L;
        final String guestName = "Bob";

        final JoinPartyDTO joinPartyDto = new JoinPartyDTO();
        joinPartyDto.setPartyId(partyId);
        joinPartyDto.setGuestName(guestName);

        final Party party = new Party();
        when(partyRepositoryMock.findById(partyId)).thenReturn(Optional.of(party));

        final Guest savedGuest = new Guest();
        savedGuest.setName(guestName);
        savedGuest.setParty(party);

        final ArgumentCaptor<Guest> guestArgumentCaptor = ArgumentCaptor.forClass(Guest.class);
        when(guestRepositoryMock.saveAndFlush(guestArgumentCaptor.capture())).thenReturn(savedGuest);

        // when
        testSubject.createGuest(joinPartyDto);

        // then
        final Guest resultedSavedGuest = guestArgumentCaptor.getValue();
        assertThat(resultedSavedGuest.getParty()).isEqualTo(party);
    }

    @Test
    public void createGuestThrowsExceptionIfPartyNotFound() {
        // given
        final long partyId = 1L;
        final String guestName = "Bob";

        final JoinPartyDTO joinPartyDto = new JoinPartyDTO();
        joinPartyDto.setPartyId(partyId);
        joinPartyDto.setGuestName(guestName);

        // when
        assertThatCode(() -> testSubject.createGuest(joinPartyDto))
                .isExactlyInstanceOf(PartyNotFoundException.class)
                .hasMessage("Party mit der ID '1' konnte nicht gefunden werden.");
    }

    @Test
    public void findPartyWithPartyId() {
        // given
        final long partyId = 1L;
        final String partyName = "Partyname";

        final Party partyMock = mock(Party.class);
        when(partyMock.getId()).thenReturn(partyId);
        when(partyMock.getName()).thenReturn(partyName);
        when(partyRepositoryMock.findById(partyId)).thenReturn(Optional.of(partyMock));

        // when
        final FindPartyDTO resultedPartyDTO = testSubject.findParty(partyId);

        // then
        assertThat(resultedPartyDTO.getId()).isEqualTo(partyId);
    }

    @Test
    public void findPartyWithPartyName() {
        // given
        final long partyId = 1L;
        final String partyName = "Partyname";

        final Party partyMock = mock(Party.class);
        when(partyMock.getId()).thenReturn(partyId);
        when(partyMock.getName()).thenReturn(partyName);
        when(partyRepositoryMock.findById(partyId)).thenReturn(Optional.of(partyMock));

        // when
        final FindPartyDTO resultedPartyDTO = testSubject.findParty(partyId);

        // then
        assertThat(resultedPartyDTO.getName()).isEqualTo(partyName);
    }

    @Test
    public void findPartyWhichHasPassword() {
        // given
        final long partyId = 1L;
        final String partyName = "Partyname";

        final Party partyMock = mock(Party.class);
        when(partyMock.getId()).thenReturn(partyId);
        when(partyMock.getName()).thenReturn(partyName);
        when(partyMock.getPassword()).thenReturn("123");
        when(partyRepositoryMock.findById(partyId)).thenReturn(Optional.of(partyMock));

        // when
        final FindPartyDTO resultedPartyDTO = testSubject.findParty(partyId);

        // then
        assertThat(resultedPartyDTO.isHasPassword()).isTrue();
    }

    @Test
    public void findPartyThroesPartyNotFoundExceptionIfPartyNotFound() {
        // given
        final long partyId = 1L;

        // when
        assertThatCode(() -> testSubject.findParty(partyId))
                .isExactlyInstanceOf(PartyNotFoundException.class)
                .hasMessage("Party mit der ID '1' konnte nicht gefunden werden.");
    }

    @Test
    public void countGuestName() {
        // given
        final String guestName = "Bob";
        when(guestRepositoryMock.countGuestByName(guestName)).thenReturn(1L);

        // when
        final Long resultedCount = testSubject.countGuestName(guestName);

        // then
        assertThat(resultedCount).isOne();
    }

    @Test
    public void getPartyPassword() {
        // given
        final long partyId = 1L;
        final String expectedPartyPassword = "123";

        final Party party = new Party();
        party.setPassword(expectedPartyPassword);
        when(partyRepositoryMock.findById(partyId)).thenReturn(Optional.of(party));

        // when
        final String resultedPartyPassword = testSubject.getPartyPassword(partyId);

        // then
        assertThat(resultedPartyPassword).isEqualTo(expectedPartyPassword);
    }

    @Test
    public void getParyPasswordThrowsExceptionIfNoPartyIsFound() {
        // given
        final long partyId = 154654L;

        // when u. then
        assertThatCode(() -> testSubject.getPartyPassword(partyId))
                .isExactlyInstanceOf(PartyNotFoundException.class)
                .hasMessage("Party mit der ID '154654' konnte nicht gefunden werden.");
    }
}