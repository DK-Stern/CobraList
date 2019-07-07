package sh.stern.cobralist.party;

import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sh.stern.cobralist.party.persistence.Party;
import sh.stern.cobralist.party.persistence.PartyRepository;
import sh.stern.cobralist.security.oauth2.user.model.User;
import sh.stern.cobralist.security.oauth2.user.repository.UserRepository;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PartyServiceTest {

    private PartyService testSubject;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private PartyRepository partyRepositoryMock;

    @Before
    public void setUp() throws Exception {
        this.testSubject = new PartyService(userRepositoryMock, partyRepositoryMock);
    }

    @Test
    public void createParty() {
        // given
        final long userId = 1L;
        final String partyName = "name";
        final String password = "password";
        final boolean downVoting = true;
        final String description = "description";
        final PartyCreationDTO partyDTO = new PartyCreationDTO();
        partyDTO.setPartyName(partyName);
        partyDTO.setPassword(password);
        partyDTO.setDownVoting(downVoting);
        partyDTO.setDescription(description);

        final User user = new User();
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(user));


        // when
        testSubject.createParty(partyDTO, userId);

        // then
        verify(userRepositoryMock).findById(userId);

        final ArgumentCaptor<Party> partyArgumentCaptor = ArgumentCaptor.forClass(Party.class);
        verify(partyRepositoryMock).save(partyArgumentCaptor.capture());
        final Party resultedParty = partyArgumentCaptor.getValue();

        final SoftAssertions softly = new SoftAssertions();
        softly.assertThat(resultedParty.getName()).isEqualTo(partyName);
        softly.assertThat(resultedParty.getPassword()).isEqualTo(password);
        softly.assertThat(resultedParty.isDownVotable()).isEqualTo(downVoting);
        softly.assertThat(resultedParty.getDescription()).isEqualTo(description);
        softly.assertThat(resultedParty.getUser()).isEqualTo(user);
        softly.assertAll();
    }
}