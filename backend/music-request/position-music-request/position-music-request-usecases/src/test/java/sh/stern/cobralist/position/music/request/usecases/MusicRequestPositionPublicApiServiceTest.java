package sh.stern.cobralist.position.music.request.usecases;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sh.stern.cobralist.party.creation.domain.TrackDTO;
import sh.stern.cobralist.position.music.request.dataaccess.port.MusicRequestPositionDataService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class MusicRequestPositionPublicApiServiceTest {

    private MusicRequestPositionPublicApiService testSubject;

    @Mock
    private MusicRequestPositionDataService musicRequestPositionDataServiceMock;

    @Before
    public void setUp() {
        testSubject = new MusicRequestPositionPublicApiService(musicRequestPositionDataServiceMock);
    }

    @Test
    public void calculateMusicRequestPosition() {
        // given
        final long playlistId = 123L;
        final int rating = 3;

        final int positionOfLastMusicRequestWithRating = 3;
        when(musicRequestPositionDataServiceMock.getPositionOfLastMusicRequestWithRating(playlistId, rating)).thenReturn(positionOfLastMusicRequestWithRating);

        // when
        final int resultedPosition = testSubject.calculateMusicRequestPosition(playlistId, rating);

        // then
        assertThat(resultedPosition).isEqualTo(positionOfLastMusicRequestWithRating + 1);
    }

    @Test
    public void getPlaylistId() {
        // given
        final String partyCode = "partyCode";
        final long expectedPlaylistId = 1234L;
        when(musicRequestPositionDataServiceMock.getPlaylistId(partyCode)).thenReturn(expectedPlaylistId);

        // when
        final Long resultedPlaylistId = testSubject.getPlaylistId(partyCode);

        // then
        assertThat(resultedPlaylistId).isEqualTo(expectedPlaylistId);
    }

    @Test
    public void persistNewMusicRequestIncreaseMusicRequestsPositions() {
        // given
        final long playlistId = 123L;
        final TrackDTO trackDTO = new TrackDTO();
        final int position = 2;

        // when
        testSubject.persistNewMusicRequest(playlistId, trackDTO, position);

        // then
        verify(musicRequestPositionDataServiceMock).increaseMusicRequestPositions(playlistId, position);
    }

    @Test
    public void persistNewMusicRequest() {
        // given
        final long playlistId = 123L;
        final TrackDTO trackDTO = new TrackDTO();
        final int position = 2;

        // when
        testSubject.persistNewMusicRequest(playlistId, trackDTO, position);

        // then
        verify(musicRequestPositionDataServiceMock).saveMusicRequest(playlistId, trackDTO, position);
    }

    @Test
    public void decreaseMusicRequestPositions() {
        // given
        final long playlistId = 4536L;

        // when
        testSubject.decreaseMusicRequestPositions(playlistId);

        // then
        verify(musicRequestPositionDataServiceMock).decreaseMusicRequestPositions(playlistId);
    }
}