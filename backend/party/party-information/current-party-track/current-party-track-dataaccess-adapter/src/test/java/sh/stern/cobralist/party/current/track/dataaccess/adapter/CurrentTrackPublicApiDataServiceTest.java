package sh.stern.cobralist.party.current.track.dataaccess.adapter;

import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sh.stern.cobralist.party.current.track.domain.ActivePartyDTO;
import sh.stern.cobralist.party.persistence.dataaccess.MusicRequestRepository;
import sh.stern.cobralist.party.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.party.persistence.domain.MusicRequest;
import sh.stern.cobralist.party.persistence.domain.Party;
import sh.stern.cobralist.party.persistence.domain.Playlist;
import sh.stern.cobralist.party.persistence.domain.User;
import sh.stern.cobralist.party.persistence.exceptions.MusicRequestNotFoundException;
import sh.stern.cobralist.party.persistence.exceptions.PartyNotFoundException;
import sh.stern.cobralist.position.music.request.api.MusicRequestPositionService;

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

    @Mock
    private MusicRequestRepository musicRequestRepositoryMock;

    @Mock
    private MusicRequestPositionService musicRequestPositionServiceMock;

    @Before
    public void setUp() {
        testSubject = new CurrentTrackPublicApiDataService(
                partyRepositoryMock,
                musicRequestRepositoryMock,
                musicRequestPositionServiceMock);
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
        firstActiveParty.setPlaylist(new Playlist());

        final Party secondActiveParty = new Party();
        final String secondPartyCode = "partyCode2";
        secondActiveParty.setPartyCode(secondPartyCode);
        final User secondPartyUser = new User();
        final String secondUsername = "max";
        secondPartyUser.setName(secondUsername);
        secondActiveParty.setUser(secondPartyUser);
        secondActiveParty.setPlaylist(new Playlist());

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
        firstActiveParty.setPlaylist(new Playlist());

        final Party secondActiveParty = new Party();
        final String secondPartyCode = "partyCode2";
        secondActiveParty.setPartyCode(secondPartyCode);
        final User secondPartyUser = new User();
        final String secondUsername = "max";
        secondPartyUser.setName(secondUsername);
        final String secondPartyUserEmail = "email2";
        secondPartyUser.setEmail(secondPartyUserEmail);
        secondActiveParty.setUser(secondPartyUser);
        secondActiveParty.setPlaylist(new Playlist());

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
        firstActiveParty.setPlaylist(new Playlist());

        final Party secondActiveParty = new Party();
        final String secondPartyCode = "partyCode2";
        secondActiveParty.setPartyCode(secondPartyCode);
        final User secondPartyUser = new User();
        final String secondUsername = "max";
        secondPartyUser.setName(secondUsername);
        final String secondPartyUserEmail = "email2";
        secondPartyUser.setEmail(secondPartyUserEmail);
        secondActiveParty.setUser(secondPartyUser);
        secondActiveParty.setPlaylist(new Playlist());

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

    @Test
    public void musicRequestStatusPlayedIsTrue() {
        // given
        final long musicRequestId = 123L;
        final Playlist playlist = new Playlist();
        final MusicRequest musicRequest = new MusicRequest();
        musicRequest.setPlaylist(playlist);
        musicRequest.setPlayed(true);
        when(musicRequestRepositoryMock.findById(musicRequestId)).thenReturn(Optional.of(musicRequest));

        // when
        final boolean hasStatusPlayed = testSubject.hasMusicRequestStatusPlayed(musicRequestId);

        // then
        assertThat(hasStatusPlayed).isTrue();
    }

    @Test
    public void musicRequestStatusPlayedIsFalse() {
        // given

        final long musicRequestId = 456L;
        final Playlist playlist = new Playlist();
        final MusicRequest musicRequest = new MusicRequest();
        musicRequest.setPlaylist(playlist);
        musicRequest.setPlayed(false);
        when(musicRequestRepositoryMock.findById(musicRequestId)).thenReturn(Optional.of(musicRequest));

        // when
        final boolean hasStatusPlayed = testSubject.hasMusicRequestStatusPlayed(musicRequestId);

        // then
        assertThat(hasStatusPlayed).isFalse();
    }

    @Test
    public void musicRequestStatusPlayedIsTrueIfMusicRequestNotFound() {
        // given
        final long musicRequestId = 213L;
        when(musicRequestRepositoryMock.findById(musicRequestId)).thenReturn(Optional.empty());

        // when
        final boolean hasStatusPlayed = testSubject.hasMusicRequestStatusPlayed(musicRequestId);

        // then
        assertThat(hasStatusPlayed).isTrue();
    }

    @Test
    public void changeMusicRequestPlayedStatus() {
        // given
        final long musicRequestId = 456L;
        final boolean isPlayedStatus = true;

        final Playlist playlist = new Playlist();
        final MusicRequest musicRequest = new MusicRequest();
        musicRequest.setPlaylist(playlist);
        musicRequest.setPosition(1);
        when(musicRequestRepositoryMock.findById(musicRequestId)).thenReturn(Optional.of(musicRequest));

        // when
        testSubject.changeMusicRequestPlayedStatus(musicRequestId, isPlayedStatus);

        // then
        verify(musicRequestRepositoryMock).saveAndFlush(musicRequest);
        assertThat(musicRequest.getPlayed()).isEqualTo(isPlayedStatus);
    }

    @Test
    public void changeMusicRequestPlayedStatusResetsRatingIfPlayedIsTrue() {
        // given
        final long musicRequestId = 789L;
        final boolean isPlayedStatus = true;

        final Playlist playlist = new Playlist();
        final MusicRequest musicRequest = new MusicRequest();
        musicRequest.setPlaylist(playlist);
        musicRequest.setUpVotes(3);
        musicRequest.setDownVotes(1);
        musicRequest.setRating(2);
        musicRequest.setPosition(8);
        when(musicRequestRepositoryMock.findById(musicRequestId)).thenReturn(Optional.of(musicRequest));

        // when
        testSubject.changeMusicRequestPlayedStatus(musicRequestId, isPlayedStatus);

        // then
        verify(musicRequestRepositoryMock).saveAndFlush(musicRequest);
        assertThat(musicRequest.getRating()).isZero();
    }

    @Test
    public void changeMusicRequestPlayedStatusResetsUpVotesIfPlayedIsTrue() {
        // given
        final long musicRequestId = 465L;
        final boolean isPlayedStatus = true;

        final Playlist playlist = new Playlist();
        final MusicRequest musicRequest = new MusicRequest();
        musicRequest.setPlaylist(playlist);
        musicRequest.setUpVotes(3);
        musicRequest.setDownVotes(1);
        musicRequest.setRating(2);
        musicRequest.setPosition(8);
        when(musicRequestRepositoryMock.findById(musicRequestId)).thenReturn(Optional.of(musicRequest));

        // when
        testSubject.changeMusicRequestPlayedStatus(musicRequestId, isPlayedStatus);

        // then
        verify(musicRequestRepositoryMock).saveAndFlush(musicRequest);
        assertThat(musicRequest.getUpVotes()).isZero();
    }

    @Test
    public void changeMusicRequestPlayedStatusResetsDownVotesIfPlayedIsTrue() {
        // given
        final long musicRequestId = 465L;
        final boolean isPlayedStatus = true;

        final Playlist playlist = new Playlist();
        final MusicRequest musicRequest = new MusicRequest();
        musicRequest.setPlaylist(playlist);
        musicRequest.setUpVotes(3);
        musicRequest.setDownVotes(1);
        musicRequest.setRating(2);
        musicRequest.setPosition(8);
        when(musicRequestRepositoryMock.findById(musicRequestId)).thenReturn(Optional.of(musicRequest));

        // when
        testSubject.changeMusicRequestPlayedStatus(musicRequestId, isPlayedStatus);

        // then
        verify(musicRequestRepositoryMock).saveAndFlush(musicRequest);
        assertThat(musicRequest.getDownVotes()).isZero();
    }

    @Test
    public void changeMusicRequestPlayedStatusResetsPositionIfPlayedIsTrue() {
        // given
        final long musicRequestId = 132L;
        final boolean isPlayedStatus = true;

        final Playlist playlist = new Playlist();
        final MusicRequest musicRequest = new MusicRequest();
        musicRequest.setPlaylist(playlist);
        musicRequest.setUpVotes(3);
        musicRequest.setDownVotes(1);
        musicRequest.setRating(2);
        musicRequest.setPosition(8);
        when(musicRequestRepositoryMock.findById(musicRequestId)).thenReturn(Optional.of(musicRequest));

        // when
        testSubject.changeMusicRequestPlayedStatus(musicRequestId, isPlayedStatus);

        // then
        verify(musicRequestRepositoryMock).saveAndFlush(musicRequest);
        assertThat(musicRequest.getPosition()).isNull();
    }

    @Test
    public void changeMusicRequestPlayedStatusDecreaseMusicRequestPositionsIfPlayedIsTrue() {
        // given
        final long musicRequestId = 123L;
        final boolean isPlayedStatus = true;
        final long playlistId = 456L;

        final Playlist playlist = new Playlist();
        playlist.setId(playlistId);
        final MusicRequest musicRequest = new MusicRequest();
        musicRequest.setPlaylist(playlist);
        musicRequest.setUpVotes(3);
        musicRequest.setDownVotes(1);
        musicRequest.setRating(2);
        final int position = 8;
        musicRequest.setPosition(position);
        when(musicRequestRepositoryMock.findById(musicRequestId)).thenReturn(Optional.of(musicRequest));

        // when
        testSubject.changeMusicRequestPlayedStatus(musicRequestId, isPlayedStatus);

        // then
        verify(musicRequestPositionServiceMock).decreaseMusicRequestPositions(playlistId, position);
    }

    @Test
    public void changeMusicRequestPlayedStatusDoesNotResetsIfPlayedIsFalse() {
        // given
        final long musicRequestId = 123L;
        final boolean isPlayedStatus = false;

        final MusicRequest musicRequest = new MusicRequest();
        final int upVotes = 3;
        musicRequest.setUpVotes(upVotes);
        final int downVotes = 1;
        musicRequest.setDownVotes(downVotes);
        final int rating = 2;
        musicRequest.setRating(rating);
        final int position = 8;
        musicRequest.setPosition(position);
        final Playlist playlist = new Playlist();
        musicRequest.setPlaylist(playlist);
        when(musicRequestRepositoryMock.findById(musicRequestId)).thenReturn(Optional.of(musicRequest));

        // when
        testSubject.changeMusicRequestPlayedStatus(musicRequestId, isPlayedStatus);

        // then
        verify(musicRequestRepositoryMock).saveAndFlush(musicRequest);
        final SoftAssertions softly = new SoftAssertions();
        softly.assertThat(musicRequest.getPosition()).isEqualTo(position);
        softly.assertThat(musicRequest.getDownVotes()).isEqualTo(downVotes);
        softly.assertThat(musicRequest.getUpVotes()).isEqualTo(upVotes);
        softly.assertThat(musicRequest.getRating()).isEqualTo(rating);
        softly.assertAll();
    }

    @Test
    public void changeMusicRequestPlayedStatusThrowsExceptionIfMusicRequestNotFound() {
        // given
        final long musicRequestId = 1233L;
        final boolean isPlayedStatus = true;

        when(musicRequestRepositoryMock.findById(musicRequestId)).thenReturn(Optional.empty());

        // when u. then
        assertThatExceptionOfType(MusicRequestNotFoundException.class)
                .isThrownBy(() -> {
                    testSubject.changeMusicRequestPlayedStatus(musicRequestId, isPlayedStatus);
                })
                .withMessage("MusicRequest mit der id '" + musicRequestId + "' konnte nicht gefunden werden.");
    }

    @Test
    public void getPlaylistStreamingId() {
        // given
        final String partyCode = "partyCode";

        final Party party = new Party();
        final Playlist playlist = new Playlist();
        final String expectedPlaylistStramingId = "playlistStramingId";
        playlist.setPlaylistId(expectedPlaylistStramingId);
        party.setPlaylist(playlist);
        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.of(party));

        // when
        final String resultedPlaylistStreamingId = testSubject.getPlaylistStreamingId(partyCode);

        // then
        assertThat(resultedPlaylistStreamingId).isEqualTo(expectedPlaylistStramingId);
    }

    @Test
    public void getPlaylistStreamingIdThrowsExceptionifPartyNotFound() {
        // given
        final String partyCode = "partyCode";
        when(partyRepositoryMock.findByPartyCode(partyCode)).thenReturn(Optional.empty());

        // when u. then
        assertThatExceptionOfType(PartyNotFoundException.class)
                .isThrownBy(() -> testSubject.getPlaylistStreamingId(partyCode))
                .withMessage("Party mit dem Code '" + partyCode + "' konnte nicht gefunden werden.");
    }
}