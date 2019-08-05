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
import sh.stern.cobralist.party.persistence.exceptions.PartyNotFoundException;
import sh.stern.cobralist.party.persistence.exceptions.UserNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
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

    @Mock
    private PartyCodeGenerator partyCodeGeneratorMock;

    @Before
    public void setUp() throws Exception {
        this.testSubject = new PartyCRUDPublicApiService(userRepositoryMock,
                partyRepositoryMock,
                partyCodeGeneratorMock);
    }

    @Test
    public void createParty() {
        // given
        final String partyCode = "A123B5";
        final long userId = 1L;
        final String partyName = "name";
        final String password = "password";
        final boolean downVoting = true;
        final String description = "description";

        final PartyCreationDTO exptectedPartyDTO = new PartyCreationDTO();
        exptectedPartyDTO.setPartyCode(partyCode);
        exptectedPartyDTO.setPartyName(partyName);
        exptectedPartyDTO.setPassword(password);
        exptectedPartyDTO.setDownVoting(downVoting);
        exptectedPartyDTO.setDescription(description);

        when(partyCodeGeneratorMock.generatePartyCode()).thenReturn(partyCode);

        final User user = new User();
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(user));

        final Party partyMock = mock(Party.class);
        when(partyMock.getPartyCode()).thenReturn(partyCode);
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

    @Test
    public void createPartyThrowsExceptionIfUserCannotBeFound() {
        // given
        final String partyCode = "A123B5";
        final long userId = 1L;
        final String partyName = "name";
        final String password = "password";
        final boolean downVoting = true;
        final String description = "description";

        final PartyCreationDTO partyDTO = new PartyCreationDTO();
        partyDTO.setPartyCode(partyCode);
        partyDTO.setPartyName(partyName);
        partyDTO.setPassword(password);
        partyDTO.setDownVoting(downVoting);
        partyDTO.setDescription(description);

        // when
        assertThatCode(() -> testSubject.createParty(partyDTO, userId))
                .isExactlyInstanceOf(UserNotFoundException.class)
                .hasMessage("Benutzer mit der Id '1' konnte nicht gefunden werden.");
    }

    @Test
    public void getParty() {
        // given
        final String partyCode = "A123B5";
        final long userId = 1L;
        final String partyName = "name";
        final String password = "password";
        final boolean downVoting = true;
        final String description = "description";

        final Party partyMock = mock(Party.class);
        when(partyMock.getPartyCode()).thenReturn(partyCode);
        when(partyMock.isDownVotable()).thenReturn(downVoting);
        when(partyMock.getName()).thenReturn(partyName);
        when(partyMock.getPassword()).thenReturn(password);
        when(partyMock.getDescription()).thenReturn(description);
        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.of(partyMock));

        PartyCreationDTO expectedPartyDto = new PartyCreationDTO();
        expectedPartyDto.setPartyCode(partyMock.getPartyCode());
        expectedPartyDto.setDownVoting(partyMock.isDownVotable());
        expectedPartyDto.setPartyName(partyMock.getName());
        expectedPartyDto.setPassword(partyMock.getPassword());
        expectedPartyDto.setDescription(partyMock.getDescription());

        // when
        final PartyCreationDTO resultedDto = testSubject.getParty(partyCode);

        // then
        assertThat(resultedDto).isEqualToComparingFieldByField(expectedPartyDto);
    }

    @Test
    public void getPartyThrowsPartyNotFoundExceptionIfPartyNotFound() {
        // given
        final String partyCode = "A123B5";

        // when
        assertThatCode(() -> testSubject.getParty(partyCode))
                .isExactlyInstanceOf(PartyNotFoundException.class)
                .hasMessage("Party mit dem Code 'A123B5' konnte nicht gefunden werden.");
    }
}