package sh.stern.cobralist.party.crud.adapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sh.stern.cobralist.party.crud.api.PartyCreationDTO;
import sh.stern.cobralist.party.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.party.persistence.dataaccess.UserRepository;
import sh.stern.cobralist.party.persistence.domain.Party;
import sh.stern.cobralist.party.persistence.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PartyCRUDPublicApiServiceTest {

    private PartyCRUDPublicApiService testSubject;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private PartyRepository partyRepositoryMock;

    @Before
    public void setUp() throws Exception {
        this.testSubject = new PartyCRUDPublicApiService(userRepositoryMock, partyRepositoryMock);
    }

    @Test
    public void createParty() {
        // given
        final Long partyId = 1123L;
        final long userId = 1L;
        final String partyName = "name";
        final String password = "password";
        final boolean downVoting = true;
        final String description = "description";

        final PartyCreationDTO exptectedPartyDTO = new PartyCreationDTO();
        exptectedPartyDTO.setPartyId(partyId.toString());
        exptectedPartyDTO.setPartyName(partyName);
        exptectedPartyDTO.setPassword(password);
        exptectedPartyDTO.setDownVoting(downVoting);
        exptectedPartyDTO.setDescription(description);

        final User user = new User();
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(user));

        final Party partyMock = mock(Party.class);
        when(partyMock.getId()).thenReturn(partyId);
        when(partyMock.isDownVotable()).thenReturn(downVoting);
        when(partyMock.getName()).thenReturn(partyName);
        when(partyMock.getPassword()).thenReturn(password);
        when(partyMock.getDescription()).thenReturn(description);
        when(partyRepositoryMock.save(any(Party.class))).thenReturn(partyMock);

        // when
        final PartyCreationDTO resultedDto = testSubject.createParty(exptectedPartyDTO, userId);

        // then
        assertThat(resultedDto).isEqualToComparingFieldByField(exptectedPartyDTO);
    }
}