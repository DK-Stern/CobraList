package sh.stern.cobralist.party.music.requests;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MusicRequestPublicApiServiceTest {

    private MusicRequestPublicApiService testSubject;

    @Mock
    private MusicRequestDataService musicRequestDataServiceMock;

    @Before
    public void setUp() {
        testSubject = new MusicRequestPublicApiService(musicRequestDataServiceMock);
    }

    @Test
    public void getMusicRequests() {
        // given
        final String partyCode = "partyCode";
        final long participantId = 12L;
        final boolean isUser = true;
        final Long playlistId = 132L;
        when(musicRequestDataServiceMock.getPlaylistId(partyCode)).thenReturn(playlistId);

        final List<MusicRequestDTO> expectedMusicRequests = Collections.singletonList(new MusicRequestDTO());
        when(musicRequestDataServiceMock.getMusicRequests(playlistId, participantId, isUser)).thenReturn(expectedMusicRequests);

        // when
        final List<MusicRequestDTO> resultedMusicRequests = testSubject.getMusicRequests(partyCode, participantId, isUser);

        // then
        Assertions.assertThat(resultedMusicRequests).isEqualTo(expectedMusicRequests);
    }
}