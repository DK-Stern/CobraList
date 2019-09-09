package sh.stern.cobralist.party.creation.dataaccess.adapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sh.stern.cobralist.party.creation.dataaccess.adapter.mapper.PartyToPartyDTOMapper;
import sh.stern.cobralist.party.creation.domain.PartyDTO;
import sh.stern.cobralist.party.persistence.dataaccess.MusicRequestRepository;
import sh.stern.cobralist.party.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.party.persistence.dataaccess.PlaylistRepository;
import sh.stern.cobralist.party.persistence.dataaccess.UserRepository;
import sh.stern.cobralist.party.persistence.domain.Party;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PartyCreationPublicApiDataServiceTest {

    private PartyCreationPublicApiDataService testSubject;

    @Mock
    private PartyRepository partyRepositoryMock;

    @Mock
    private PartyToPartyDTOMapper partyToPartyDTOMapperMock;

    @Mock
    private PlaylistRepository playlistRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private MusicRequestRepository musicRequestRepositoryMock;

    @Before
    public void setUp() {
        testSubject = new PartyCreationPublicApiDataService(partyRepositoryMock,
                playlistRepositoryMock,
                userRepositoryMock,
                musicRequestRepositoryMock,
                partyToPartyDTOMapperMock);
    }

    @Test
    public void getParty() {
        // given
        final String partyCode = "123456";

        final Party party = new Party();
        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.of(party));

        final PartyDTO expectedPartyDTO = new PartyDTO();
        when(partyToPartyDTOMapperMock.map(party)).thenReturn(expectedPartyDTO);

        // when
        final PartyDTO resultedPartyDTO = testSubject.getParty(partyCode);

        // then
        assertThat(resultedPartyDTO).isEqualTo(expectedPartyDTO);
    }
}