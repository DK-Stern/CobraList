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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
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

        when(musicRequestRepositoryMock.findByPlaylist_IdAndIsPlayed(playlistId, false)).thenReturn(Collections.singletonList(musicRequest));

        final MusicRequestDTO expectedMusicRequestDTO = new MusicRequestDTO();
        when(musicRequestToMusicRequestDTOMapperMock.map(musicRequest)).thenReturn(expectedMusicRequestDTO);

        when(voteRepositoryMock.findByUser_Id(participantId)).thenReturn(Collections.emptyList());

        // when
        final List<MusicRequestDTO> resultedMusicRequests = testSubject.getMusicRequests(playlistId, participantId, isUser);

        // then
        assertThat(resultedMusicRequests).containsExactly(expectedMusicRequestDTO);
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

        when(musicRequestRepositoryMock.findByPlaylist_IdAndIsPlayed(playlistId, false)).thenReturn(Collections.singletonList(musicRequest));

        final MusicRequestDTO expectedMusicRequestDTO = new MusicRequestDTO();
        expectedMusicRequestDTO.setUpVotes(2);
        expectedMusicRequestDTO.setDownVotes(1);
        when(musicRequestToMusicRequestDTOMapperMock.map(musicRequest)).thenReturn(expectedMusicRequestDTO);

        when(voteRepositoryMock.findByGuest_Id(participantId)).thenReturn(Collections.emptyList());

        // when
        final List<MusicRequestDTO> resultedMusicRequests = testSubject.getMusicRequests(playlistId, participantId, isUser);

        // then
        assertThat(resultedMusicRequests).containsExactly(expectedMusicRequestDTO);
    }

    @Test
    public void getMusicRequestsWhichIsAlreadyVotedByGuest() {
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

        when(musicRequestRepositoryMock.findByPlaylist_IdAndIsPlayed(playlistId, false)).thenReturn(Collections.singletonList(musicRequest));

        final MusicRequestDTO expectedMusicRequestDTO = new MusicRequestDTO();
        when(musicRequestToMusicRequestDTOMapperMock.map(musicRequest)).thenReturn(expectedMusicRequestDTO);

        final Vote vote = new Vote();
        vote.setMusicRequest(musicRequest);
        when(voteRepositoryMock.findByGuest_Id(participantId)).thenReturn(Collections.singletonList(vote));

        // when
        final List<MusicRequestDTO> resultedMusicRequests = testSubject.getMusicRequests(playlistId, participantId, isUser);

        // then
        assertThat(resultedMusicRequests)
                .hasSize(1)
                .first().extracting(MusicRequestDTO::getAlreadyVoted).isEqualTo(true);
    }

    @Test
    public void getMusicRequestsWhichIsAlreadyVotedByUser() {
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

        when(musicRequestRepositoryMock.findByPlaylist_IdAndIsPlayed(playlistId, false)).thenReturn(Collections.singletonList(musicRequest));

        final MusicRequestDTO expectedMusicRequestDTO = new MusicRequestDTO();
        when(musicRequestToMusicRequestDTOMapperMock.map(musicRequest)).thenReturn(expectedMusicRequestDTO);

        final Vote vote = new Vote();
        vote.setMusicRequest(musicRequest);
        when(voteRepositoryMock.findByUser_Id(participantId)).thenReturn(Collections.singletonList(vote));

        // when
        final List<MusicRequestDTO> resultedMusicRequests = testSubject.getMusicRequests(playlistId, participantId, isUser);

        // then
        assertThat(resultedMusicRequests)
                .hasSize(1)
                .first().extracting(MusicRequestDTO::getAlreadyVoted).isEqualTo(true);
    }
}