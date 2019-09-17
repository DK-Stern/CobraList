package sh.stern.cobralist.party.join.dataaccess.adapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sh.stern.cobralist.party.join.dataaccess.port.GuestCreatedDTO;
import sh.stern.cobralist.party.join.domain.FindPartyDTO;
import sh.stern.cobralist.party.join.domain.JoinPartyDTO;
import sh.stern.cobralist.party.persistence.dataaccess.GuestRepository;
import sh.stern.cobralist.party.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.party.persistence.domain.Guest;
import sh.stern.cobralist.party.persistence.domain.Party;
import sh.stern.cobralist.party.persistence.exceptions.PartyNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
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
        final String partyCode = "A123D1";
        final String guestName = "Bob";

        final JoinPartyDTO joinPartyDto = new JoinPartyDTO();
        joinPartyDto.setPartyCode(partyCode);
        joinPartyDto.setGuestName(guestName);

        final Party party = new Party();
        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.of(party));

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
    public void createGuestDTOWithGuestId() {
        // given
        final String partyCode = "A123D1";
        final String guestName = "Bob";
        final long guestId = 123L;

        final JoinPartyDTO joinPartyDto = new JoinPartyDTO();
        joinPartyDto.setPartyCode(partyCode);
        joinPartyDto.setGuestName(guestName);

        final Party partyMock = mock(Party.class);
        when(partyMock.getPartyCode()).thenReturn(partyCode);
        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.of(partyMock));

        final Guest savedGuest = new Guest();
        savedGuest.setId(guestId);
        savedGuest.setName(guestName);
        savedGuest.setParty(partyMock);

        when(guestRepositoryMock.saveAndFlush(any(Guest.class))).thenReturn(savedGuest);

        // when
        final GuestCreatedDTO resultedGuestDTO = testSubject.createGuest(joinPartyDto);

        // then
        assertThat(resultedGuestDTO.getGuestId()).isEqualTo(guestId);
    }

    @Test
    public void createGuestDTOWithPartyCode() {
        // given
        final String partyCode = "A123D1";
        final String guestName = "Bob";

        final JoinPartyDTO joinPartyDto = new JoinPartyDTO();
        joinPartyDto.setPartyCode(partyCode);
        joinPartyDto.setGuestName(guestName);

        final Party partyMock = mock(Party.class);
        when(partyMock.getPartyCode()).thenReturn(partyCode);
        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.of(partyMock));

        final Guest savedGuest = new Guest();
        savedGuest.setName(guestName);
        savedGuest.setParty(partyMock);

        when(guestRepositoryMock.saveAndFlush(any(Guest.class))).thenReturn(savedGuest);

        // when
        final GuestCreatedDTO resultedGuestDTO = testSubject.createGuest(joinPartyDto);

        // then
        assertThat(resultedGuestDTO.getPartyCode()).isEqualTo(partyCode);
    }

    @Test
    public void saveGuestOnCreationWithName() {
        // given
        final String partyCode = "A123D1";
        final String guestName = "Bob";

        final JoinPartyDTO joinPartyDto = new JoinPartyDTO();
        joinPartyDto.setPartyCode(partyCode);
        joinPartyDto.setGuestName(guestName);

        final Party party = new Party();
        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.of(party));

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
        final String partyCode = "A123D1";
        final String guestName = "Bob";

        final JoinPartyDTO joinPartyDto = new JoinPartyDTO();
        joinPartyDto.setPartyCode(partyCode);
        joinPartyDto.setGuestName(guestName);

        final Party party = new Party();
        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.of(party));

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
        final String partyCode = "A123D1";
        final String guestName = "Bob";

        final JoinPartyDTO joinPartyDto = new JoinPartyDTO();
        joinPartyDto.setPartyCode(partyCode);
        joinPartyDto.setGuestName(guestName);

        // when
        assertThatCode(() -> testSubject.createGuest(joinPartyDto))
                .isExactlyInstanceOf(PartyNotFoundException.class)
                .hasMessage("Party mit dem Code 'A123D1' konnte nicht gefunden werden.");
    }

    @Test
    public void findPartyWithpartyCode() {
        // given
        final String partyCode = "A123D1";
        final String partyName = "Partyname";

        final Party partyMock = mock(Party.class);
        when(partyMock.getPartyCode()).thenReturn(partyCode);
        when(partyMock.getName()).thenReturn(partyName);
        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.of(partyMock));

        // when
        final FindPartyDTO resultedPartyDTO = testSubject.findParty(partyCode);

        // then
        assertThat(resultedPartyDTO.getPartyCode()).isEqualTo(partyCode);
    }

    @Test
    public void findPartyWithPartyName() {
        // given
        final String partyCode = "A123D1";
        final String partyName = "Partyname";

        final Party partyMock = mock(Party.class);
        when(partyMock.getPartyCode()).thenReturn(partyCode);
        when(partyMock.getName()).thenReturn(partyName);
        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.of(partyMock));

        // when
        final FindPartyDTO resultedPartyDTO = testSubject.findParty(partyCode);

        // then
        assertThat(resultedPartyDTO.getName()).isEqualTo(partyName);
    }

    @Test
    public void findPartyWhichHasPassword() {
        // given
        final String partyCode = "A123D1";
        final String partyName = "Partyname";

        final Party partyMock = mock(Party.class);
        when(partyMock.getPartyCode()).thenReturn(partyCode);
        when(partyMock.getName()).thenReturn(partyName);
        when(partyMock.getPassword()).thenReturn("123");
        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.of(partyMock));

        // when
        final FindPartyDTO resultedPartyDTO = testSubject.findParty(partyCode);

        // then
        assertThat(resultedPartyDTO.isHasPassword()).isTrue();
    }

    @Test
    public void findPartyThroesPartyNotFoundExceptionIfPartyNotFound() {
        // given
        final String partyCode = "A123D1";

        // when
        assertThatCode(() -> testSubject.findParty(partyCode))
                .isExactlyInstanceOf(PartyNotFoundException.class)
                .hasMessage("Party mit dem Code 'A123D1' konnte nicht gefunden werden.");
    }

    @Test
    public void countGuestName() {
        // given
        final String partyCode = "partyCode";
        final String guestName = "Bob";
        final Party party = new Party();
        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.of(party));

        when(guestRepositoryMock.countGuestByNameAndParty(guestName, party)).thenReturn(1L);

        // when
        final Long resultedCount = testSubject.countGuestName(guestName, partyCode);

        // then
        assertThat(resultedCount).isOne();
    }

    @Test
    public void countGuestNameThrowsExceptionifPartyNotFound() {
        // given
        final String guestName = "guestName";
        final String partyCode = "partyCode";

        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.empty());

        // when u. then
        assertThatExceptionOfType(PartyNotFoundException.class)
                .describedAs("")
                .isThrownBy(() -> {
                    testSubject.countGuestName(guestName, partyCode);
                });
    }

    @Test
    public void getPartyPassword() {
        // given
        final String partyCode = "A123D1";
        final String expectedPartyPassword = "123";

        final Party party = new Party();
        party.setPassword(expectedPartyPassword);
        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.of(party));

        // when
        final String resultedPartyPassword = testSubject.getPartyPassword(partyCode);

        // then
        assertThat(resultedPartyPassword).isEqualTo(expectedPartyPassword);
    }

    @Test
    public void getParyPasswordThrowsExceptionIfNoPartyIsFound() {
        // given
        final String partyCode = "A123D1";

        // when u. then
        assertThatCode(() -> testSubject.getPartyPassword(partyCode))
                .isExactlyInstanceOf(PartyNotFoundException.class)
                .hasMessage("Party mit dem Code 'A123D1' konnte nicht gefunden werden.");
    }
}