package sh.stern.cobralist.party.music.requests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sh.stern.cobralist.party.persistence.dataaccess.MusicRequestRepository;
import sh.stern.cobralist.party.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.party.persistence.dataaccess.PlaylistRepository;
import sh.stern.cobralist.party.persistence.dataaccess.VoteRepository;
import sh.stern.cobralist.party.persistence.domain.MusicRequest;
import sh.stern.cobralist.party.persistence.domain.Party;
import sh.stern.cobralist.party.persistence.domain.Playlist;
import sh.stern.cobralist.party.persistence.domain.Vote;
import sh.stern.cobralist.party.persistence.exceptions.PartyNotFoundException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@SuppressWarnings("squid:S1192") // duplicate literals in tests are ok
@RunWith(MockitoJUnitRunner.class)
public class MusicRequestPublicApiDataServiceTest {

    private MusicRequestPublicApiDataService testSubject;

    @Mock
    private PartyRepository partyRepositoryMock;

    @Mock
    private MusicRequestRepository musicRequestRepositoryMock;

    @Mock
    private VoteRepository voteRepositoryMock;

    @Mock
    private MusicRequestToMusicRequestDTOMapper musicRequestToMusicRequestDTOMapperMock;

    @Mock
    private PlaylistRepository playlistRepositoryMock;

    @Before
    public void setUp() {
        testSubject = new MusicRequestPublicApiDataService(
                partyRepositoryMock,
                playlistRepositoryMock,
                musicRequestRepositoryMock,
                voteRepositoryMock,
                musicRequestToMusicRequestDTOMapperMock);
    }

    @Test
    public void getPlaylistId() {
        // given
        final long expectedPlaylistId = 12L;
        final String partyCode = "partyCode";

        final Party party = new Party();
        final Playlist playlist = new Playlist();
        playlist.setId(expectedPlaylistId);
        party.setPlaylist(playlist);
        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.of(party));

        // when
        final Long resultedplaylistId = testSubject.getPlaylistId(partyCode);

        // then
        assertThat(resultedplaylistId).isEqualTo(expectedPlaylistId);
    }

    @Test
    public void getPlaylistIdThrowsExceptionIfPartyNotExist() {
        // given
        final String partyCode = "partyCode";

        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.empty());

        // when
        assertThatExceptionOfType(PartyNotFoundException.class)
                .describedAs("Party mit dem Code '" + partyCode + "' konnte nicht gefunden werden.")
                .isThrownBy(() -> testSubject.getPlaylistId(partyCode));
    }

    @Test
    public void getMusicRequestsForUser() {
        // given
        final long playlistId = 12L;
        final long participantId = 12L;
        final boolean isUser = true;

        final MusicRequest musicRequest = new MusicRequest();
        final int duration = 12346;
        musicRequest.setDuration(duration);
        final String uri = "uri";
        musicRequest.setUri(uri);
        final List<String> artists = Collections.singletonList("The Hellfreaks");
        musicRequest.setArtist(artists);
        final String title = "Men in Grey";
        musicRequest.setTitle(title);
        final int imageWidth = 64;
        musicRequest.setImageWidth(imageWidth);
        final int imageHeight = 64;
        musicRequest.setImageHeight(imageHeight);
        final String imageUrl = "imageUrl";
        musicRequest.setImageUrl(imageUrl);
        final String trackId = "trackId";
        musicRequest.setTrackId(trackId);
        final long musicRequestId = 132L;
        musicRequest.setId(musicRequestId);
        final Vote upVote = new Vote();
        upVote.setMusicRequest(musicRequest);
        upVote.setDownVote(false);
        final Vote downVote = new Vote();
        downVote.setMusicRequest(musicRequest);
        downVote.setDownVote(true);
        musicRequest.setVotes(Arrays.asList(upVote, downVote));

        when(musicRequestRepositoryMock.findByPlaylist_Id(playlistId)).thenReturn(Collections.singletonList(musicRequest));

        when(voteRepositoryMock.countByMusicRequest_IdAndIsDownVote(musicRequestId, true)).thenReturn(1L);

        final MusicRequestDTO expectedMusicRequestDTO = new MusicRequestDTO();
        expectedMusicRequestDTO.setAllVotes(2L);
        expectedMusicRequestDTO.setDownVotes(1L);
        when(musicRequestToMusicRequestDTOMapperMock.map(musicRequest)).thenReturn(expectedMusicRequestDTO);

        when(voteRepositoryMock.countByMusicRequest_IdAndUser_Id(musicRequestId, participantId)).thenReturn(0L);

        // when
        final List<MusicRequestDTO> resultedMusicRequests = testSubject.getMusicRequests(playlistId, participantId, isUser);

        // then
        assertThat(resultedMusicRequests).containsExactly(expectedMusicRequestDTO);
    }

    @Test
    public void getMusicRequestsForUserHasUpVotesOne() {
        // given
        final long playlistId = 12L;
        final long participantId = 12L;
        final boolean isUser = true;

        final MusicRequest musicRequest = new MusicRequest();
        final int duration = 12346;
        musicRequest.setDuration(duration);
        final String uri = "uri";
        musicRequest.setUri(uri);
        final List<String> artists = Collections.singletonList("The Hellfreaks");
        musicRequest.setArtist(artists);
        final String title = "Men in Grey";
        musicRequest.setTitle(title);
        final int imageWidth = 64;
        musicRequest.setImageWidth(imageWidth);
        final int imageHeight = 64;
        musicRequest.setImageHeight(imageHeight);
        final String imageUrl = "imageUrl";
        musicRequest.setImageUrl(imageUrl);
        final String trackId = "trackId";
        musicRequest.setTrackId(trackId);
        final long musicRequestId = 132L;
        musicRequest.setId(musicRequestId);
        final Vote upVote = new Vote();
        upVote.setMusicRequest(musicRequest);
        upVote.setDownVote(false);
        final Vote downVote = new Vote();
        downVote.setMusicRequest(musicRequest);
        downVote.setDownVote(true);
        musicRequest.setVotes(Arrays.asList(upVote, downVote));

        when(musicRequestRepositoryMock.findByPlaylist_Id(playlistId)).thenReturn(Collections.singletonList(musicRequest));

        when(voteRepositoryMock.countByMusicRequest_IdAndIsDownVote(musicRequestId, true)).thenReturn(1L);

        final MusicRequestDTO expectedMusicRequestDTO = new MusicRequestDTO();
        expectedMusicRequestDTO.setAllVotes(2L);
        expectedMusicRequestDTO.setDownVotes(1L);
        when(musicRequestToMusicRequestDTOMapperMock.map(musicRequest)).thenReturn(expectedMusicRequestDTO);

        when(voteRepositoryMock.countByMusicRequest_IdAndUser_Id(musicRequestId, participantId)).thenReturn(0L);

        // when
        final List<MusicRequestDTO> resultedMusicRequests = testSubject.getMusicRequests(playlistId, participantId, isUser);

        // then
        assertThat(resultedMusicRequests).extracting(MusicRequestDTO::getUpVotes).containsExactly(1L);
    }

    @Test
    public void getMusicRequestsForGuest() {
        // given
        final long playlistId = 12L;
        final long participantId = 12L;
        final boolean isUser = false;

        final MusicRequest musicRequest = new MusicRequest();
        final int duration = 12346;
        musicRequest.setDuration(duration);
        final String uri = "uri";
        musicRequest.setUri(uri);
        final List<String> artists = Collections.singletonList("The Hellfreaks");
        musicRequest.setArtist(artists);
        final String title = "Men in Grey";
        musicRequest.setTitle(title);
        final int imageWidth = 64;
        musicRequest.setImageWidth(imageWidth);
        final int imageHeight = 64;
        musicRequest.setImageHeight(imageHeight);
        final String imageUrl = "imageUrl";
        musicRequest.setImageUrl(imageUrl);
        final String trackId = "trackId";
        musicRequest.setTrackId(trackId);
        final long musicRequestId = 132L;
        musicRequest.setId(musicRequestId);
        final Vote upVote = new Vote();
        upVote.setMusicRequest(musicRequest);
        upVote.setDownVote(false);
        final Vote downVote = new Vote();
        downVote.setMusicRequest(musicRequest);
        downVote.setDownVote(true);
        musicRequest.setVotes(Arrays.asList(upVote, downVote));

        when(musicRequestRepositoryMock.findByPlaylist_Id(playlistId)).thenReturn(Collections.singletonList(musicRequest));

        when(voteRepositoryMock.countByMusicRequest_IdAndIsDownVote(musicRequestId, true)).thenReturn(1L);

        final MusicRequestDTO expectedMusicRequestDTO = new MusicRequestDTO();
        expectedMusicRequestDTO.setAllVotes(2L);
        expectedMusicRequestDTO.setDownVotes(1L);
        when(musicRequestToMusicRequestDTOMapperMock.map(musicRequest)).thenReturn(expectedMusicRequestDTO);

        when(voteRepositoryMock.countByMusicRequest_IdAndGuest_Id(musicRequestId, participantId)).thenReturn(0L);

        // when
        final List<MusicRequestDTO> resultedMusicRequests = testSubject.getMusicRequests(playlistId, participantId, isUser);

        // then
        assertThat(resultedMusicRequests).containsExactly(expectedMusicRequestDTO);
    }

    @Test
    public void getMusicRequestsForGuestHasUpVotesOne() {
        // given
        final long playlistId = 12L;
        final long participantId = 12L;
        final boolean isUser = false;

        final MusicRequest musicRequest = new MusicRequest();
        final int duration = 12346;
        musicRequest.setDuration(duration);
        final String uri = "uri";
        musicRequest.setUri(uri);
        final List<String> artists = Collections.singletonList("The Hellfreaks");
        musicRequest.setArtist(artists);
        final String title = "Men in Grey";
        musicRequest.setTitle(title);
        final int imageWidth = 64;
        musicRequest.setImageWidth(imageWidth);
        final int imageHeight = 64;
        musicRequest.setImageHeight(imageHeight);
        final String imageUrl = "imageUrl";
        musicRequest.setImageUrl(imageUrl);
        final String trackId = "trackId";
        musicRequest.setTrackId(trackId);
        final long musicRequestId = 132L;
        musicRequest.setId(musicRequestId);
        final Vote upVote = new Vote();
        upVote.setMusicRequest(musicRequest);
        upVote.setDownVote(false);
        final Vote downVote = new Vote();
        downVote.setMusicRequest(musicRequest);
        downVote.setDownVote(true);
        musicRequest.setVotes(Arrays.asList(upVote, downVote));

        when(musicRequestRepositoryMock.findByPlaylist_Id(playlistId)).thenReturn(Collections.singletonList(musicRequest));

        when(voteRepositoryMock.countByMusicRequest_IdAndIsDownVote(musicRequestId, true)).thenReturn(1L);

        final MusicRequestDTO expectedMusicRequestDTO = new MusicRequestDTO();
        expectedMusicRequestDTO.setAllVotes(2L);
        expectedMusicRequestDTO.setDownVotes(1L);
        when(musicRequestToMusicRequestDTOMapperMock.map(musicRequest)).thenReturn(expectedMusicRequestDTO);

        when(voteRepositoryMock.countByMusicRequest_IdAndGuest_Id(musicRequestId, participantId)).thenReturn(0L);

        // when
        final List<MusicRequestDTO> resultedMusicRequests = testSubject.getMusicRequests(playlistId, participantId, isUser);

        // then
        assertThat(resultedMusicRequests).extracting(MusicRequestDTO::getUpVotes).containsExactly(1L);
    }

    @Test
    public void getMusicRequestsForGuestHasRatingZero() {
        // given
        final long playlistId = 12L;
        final long participantId = 12L;
        final boolean isUser = false;

        final MusicRequest musicRequest = new MusicRequest();
        final int duration = 12346;
        musicRequest.setDuration(duration);
        final String uri = "uri";
        musicRequest.setUri(uri);
        final List<String> artists = Collections.singletonList("The Hellfreaks");
        musicRequest.setArtist(artists);
        final String title = "Men in Grey";
        musicRequest.setTitle(title);
        final int imageWidth = 64;
        musicRequest.setImageWidth(imageWidth);
        final int imageHeight = 64;
        musicRequest.setImageHeight(imageHeight);
        final String imageUrl = "imageUrl";
        musicRequest.setImageUrl(imageUrl);
        final String trackId = "trackId";
        musicRequest.setTrackId(trackId);
        final long musicRequestId = 132L;
        musicRequest.setId(musicRequestId);
        final Vote upVote = new Vote();
        upVote.setMusicRequest(musicRequest);
        upVote.setDownVote(false);
        final Vote downVote = new Vote();
        downVote.setMusicRequest(musicRequest);
        downVote.setDownVote(true);
        musicRequest.setVotes(Arrays.asList(upVote, downVote));

        when(musicRequestRepositoryMock.findByPlaylist_Id(playlistId)).thenReturn(Collections.singletonList(musicRequest));

        when(voteRepositoryMock.countByMusicRequest_IdAndIsDownVote(musicRequestId, true)).thenReturn(1L);

        final MusicRequestDTO expectedMusicRequestDTO = new MusicRequestDTO();
        expectedMusicRequestDTO.setAllVotes(2L);
        expectedMusicRequestDTO.setDownVotes(1L);
        when(musicRequestToMusicRequestDTOMapperMock.map(musicRequest)).thenReturn(expectedMusicRequestDTO);

        when(voteRepositoryMock.countByMusicRequest_IdAndGuest_Id(musicRequestId, participantId)).thenReturn(0L);

        // when
        final List<MusicRequestDTO> resultedMusicRequests = testSubject.getMusicRequests(playlistId, participantId, isUser);

        // then
        assertThat(resultedMusicRequests).extracting(MusicRequestDTO::getRating).containsExactly(0L);
    }
}