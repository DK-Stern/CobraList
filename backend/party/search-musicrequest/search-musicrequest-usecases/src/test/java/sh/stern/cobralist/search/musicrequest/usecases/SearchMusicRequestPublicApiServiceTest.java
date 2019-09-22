package sh.stern.cobralist.search.musicrequest.usecases;

import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sh.stern.cobralist.party.creation.domain.TrackDTO;
import sh.stern.cobralist.party.security.api.PartySecurityService;
import sh.stern.cobralist.search.musicrequest.dataaccess.port.SearchMusicRequestDataService;
import sh.stern.cobralist.search.musicrequest.domain.SearchMusicRequestDTO;
import sh.stern.cobralist.streaming.api.SearchTrackStreamingService;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchMusicRequestPublicApiServiceTest {

    private SearchMusicRequestPublicApiService testSubject;

    @Mock
    private PartySecurityService partySecurityServiceMock;

    @Mock
    private SearchTrackStreamingService searchTrackStreamingServiceMock;

    @Mock
    private SearchMusicRequestDataService searchMusicRequestDataServiceMock;

    @Mock
    private TrackDTOToSearchMusicRequestDTOMapper trackDTOToSearchMusicRequestDTOMapperMock;

    @Before
    public void setUp() {
        testSubject = new SearchMusicRequestPublicApiService(
                partySecurityServiceMock,
                searchTrackStreamingServiceMock,
                searchMusicRequestDataServiceMock,
                trackDTOToSearchMusicRequestDTOMapperMock);
    }

    @Test
    public void verifySearchMusicRequestChecksPermission() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final String partyCode = "partyCode";
        final String searchString = "searchString";

        // when
        testSubject.searchMusicRequest(userPrincipal, partyCode, searchString);

        // then
        verify(partySecurityServiceMock).checkGetPartyInformationPermission(userPrincipal, partyCode);
    }

    @Test
    public void searchMusicRequest() {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final String username = "username";
        userPrincipal.setEmail(username);
        final String partyCode = "partyCode";
        final String searchString = "searchString";

        final TrackDTO trackDTO = new TrackDTO();
        when(searchTrackStreamingServiceMock.searchTrack(username, searchString)).thenReturn(Collections.singletonList(trackDTO));

        final long playlistId = 123L;
        when(searchMusicRequestDataServiceMock.getPlaylistId(partyCode)).thenReturn(playlistId);

        final SearchMusicRequestDTO expectedSearchMusicRequestDTO = new SearchMusicRequestDTO();
        final String trackId = "trackId";
        expectedSearchMusicRequestDTO.setTrackId(trackId);
        when(trackDTOToSearchMusicRequestDTOMapperMock.map(trackDTO)).thenReturn(expectedSearchMusicRequestDTO);
        when(searchMusicRequestDataServiceMock.isMusicRequestAlreadyInPlaylist(playlistId, trackId)).thenReturn(false);

        // when
        final List<SearchMusicRequestDTO> resultedSearchMusicRequestDTOS = testSubject.searchMusicRequest(userPrincipal, partyCode, searchString);

        // then
        final SoftAssertions softly = new SoftAssertions();
        softly.assertThat(resultedSearchMusicRequestDTOS).containsExactly(expectedSearchMusicRequestDTO);
        softly.assertThat(expectedSearchMusicRequestDTO.isAlreadyInPlaylist()).isFalse();
        softly.assertAll();
    }
}