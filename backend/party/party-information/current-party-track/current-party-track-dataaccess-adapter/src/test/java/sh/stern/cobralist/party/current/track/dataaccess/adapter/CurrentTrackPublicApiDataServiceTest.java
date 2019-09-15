package sh.stern.cobralist.party.current.track.dataaccess.adapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sh.stern.cobralist.party.current.track.domain.ActivePartyDTO;
import sh.stern.cobralist.party.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.party.persistence.domain.Party;
import sh.stern.cobralist.party.persistence.domain.User;
import sh.stern.cobralist.party.persistence.exceptions.PartyNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("squid:S1192") // duplicate literals in tests are ok
@RunWith(MockitoJUnitRunner.class)
public class CurrentTrackPublicApiDataServiceTest {

    private CurrentTrackPublicApiDataService testSubject;

    @Mock
    private PartyRepository partyRepositoryMock;

    @Before
    public void setUp() {
        testSubject = new CurrentTrackPublicApiDataService(partyRepositoryMock);
    }

    @Test
    public void findAllActivePartiesPartyCodes() {
        // given
        final Party firstActiveParty = new Party();
        final String firstPartyCode = "partyCode1";
        firstActiveParty.setPartyCode(firstPartyCode);
        final User firstPartyUser = new User();
        final String firstUsername = "peter";
        firstPartyUser.setName(firstUsername);
        firstActiveParty.setUser(firstPartyUser);

        final Party secondActiveParty = new Party();
        final String secondPartyCode = "partyCode2";
        secondActiveParty.setPartyCode(secondPartyCode);
        final User secondPartyUser = new User();
        final String secondUsername = "max";
        secondPartyUser.setName(secondUsername);
        secondActiveParty.setUser(secondPartyUser);

        when(partyRepositoryMock.findByActive(true)).thenReturn(Arrays.asList(firstActiveParty, secondActiveParty));

        // when
        final List<ActivePartyDTO> activeParties = testSubject.getActiveParties();

        // then
        assertThat(activeParties).extracting(ActivePartyDTO::getPartyCode).containsExactly(firstPartyCode, secondPartyCode);
    }

    @Test
    public void findAllActivePartiesCreatorsName() {
        // given
        final Party firstActiveParty = new Party();
        final String firstPartyCode = "partyCode1";
        firstActiveParty.setPartyCode(firstPartyCode);
        final User firstPartyUser = new User();
        final String firstUsername = "peter";
        firstPartyUser.setName(firstUsername);
        final String firstPartyUserEmail = "email1";
        firstPartyUser.setEmail(firstPartyUserEmail);
        firstActiveParty.setUser(firstPartyUser);

        final Party secondActiveParty = new Party();
        final String secondPartyCode = "partyCode2";
        secondActiveParty.setPartyCode(secondPartyCode);
        final User secondPartyUser = new User();
        final String secondUsername = "max";
        secondPartyUser.setName(secondUsername);
        final String secondPartyUserEmail = "email2";
        secondPartyUser.setEmail(secondPartyUserEmail);
        secondActiveParty.setUser(secondPartyUser);

        when(partyRepositoryMock.findByActive(true)).thenReturn(Arrays.asList(firstActiveParty, secondActiveParty));

        // when
        final List<ActivePartyDTO> activeParties = testSubject.getActiveParties();

        // then
        assertThat(activeParties).extracting(ActivePartyDTO::getCreatorName).containsExactly(firstUsername, secondUsername);
    }

    @Test
    public void findAllActivePartiesCreatorsEmail() {
        // given
        final Party firstActiveParty = new Party();
        final String firstPartyCode = "partyCode1";
        firstActiveParty.setPartyCode(firstPartyCode);
        final User firstPartyUser = new User();
        final String firstUsername = "peter";
        firstPartyUser.setName(firstUsername);
        final String firstPartyUserEmail = "email1";
        firstPartyUser.setEmail(firstPartyUserEmail);
        firstActiveParty.setUser(firstPartyUser);

        final Party secondActiveParty = new Party();
        final String secondPartyCode = "partyCode2";
        secondActiveParty.setPartyCode(secondPartyCode);
        final User secondPartyUser = new User();
        final String secondUsername = "max";
        secondPartyUser.setName(secondUsername);
        final String secondPartyUserEmail = "email2";
        secondPartyUser.setEmail(secondPartyUserEmail);
        secondActiveParty.setUser(secondPartyUser);

        when(partyRepositoryMock.findByActive(true)).thenReturn(Arrays.asList(firstActiveParty, secondActiveParty));

        // when
        final List<ActivePartyDTO> activeParties = testSubject.getActiveParties();

        // then
        assertThat(activeParties).extracting(ActivePartyDTO::getCreatorEmail).containsExactly(firstPartyUserEmail, secondPartyUserEmail);
    }

    @Test
    public void changePartyActiveStatus() {
        // given
        final String partyCode = "partyCode";
        final boolean activeStatus = true;

        final Party party = new Party();
        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.of(party));

        // when
        testSubject.changePartyActiveStatus(partyCode, activeStatus);

        // then
        final ArgumentCaptor<Party> partyArgumentCaptor = ArgumentCaptor.forClass(Party.class);
        verify(partyRepositoryMock).save(partyArgumentCaptor.capture());
        final Party savedParty = partyArgumentCaptor.getValue();
        assertThat(savedParty.getActive()).isEqualTo(activeStatus);
    }

    @Test
    public void changePartyActiveStatusThrowsExceptionIfPartyNotFound() {
        // given
        final String partyCode = "partyCode";
        final boolean activeStatus = true;

        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.empty());

        // when
        assertThatExceptionOfType(PartyNotFoundException.class)
                .describedAs("Party mit dem Code '" + partyCode + "' konnte nicht gefunden werden.")
                .isThrownBy(() -> testSubject.changePartyActiveStatus(partyCode, activeStatus));
    }
}