package sh.stern.cobralist.party.music.requests;

import org.junit.Before;
import org.junit.Test;
import sh.stern.cobralist.party.persistence.domain.MusicRequest;
import sh.stern.cobralist.party.persistence.domain.Vote;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MusicRequestToMusicRequestDTOMapperTest {

    private MusicRequestToMusicRequestDTOMapper testSubject;

    @Before
    public void setUp() {
        testSubject = new MusicRequestToMusicRequestDTOMapper();
    }

    @Test
    public void mapTrackId() {
        // given
        final MusicRequest musicRequest = new MusicRequest();
        final String trackId = "trackId";
        musicRequest.setTrackId(trackId);
        musicRequest.setVotes(Collections.emptyList());

        // when
        final MusicRequestDTO musicRequestDTO = testSubject.map(musicRequest);

        // then
        assertThat(musicRequestDTO.getTrackId()).isEqualTo(trackId);
    }

    @Test
    public void mapArtists() {
        // given
        final MusicRequest musicRequest = new MusicRequest();
        final List<String> artists = Collections.singletonList("Lygo");
        musicRequest.setArtist(artists);
        musicRequest.setVotes(Collections.emptyList());

        // when
        final MusicRequestDTO musicRequestDTO = testSubject.map(musicRequest);

        // then
        assertThat(musicRequestDTO.getArtist()).isEqualTo(artists);
    }

    @Test
    public void mapTitle() {
        // given
        final MusicRequest musicRequest = new MusicRequest();
        final String title = "Kloß im Hals";
        musicRequest.setTitle(title);
        musicRequest.setVotes(Collections.emptyList());

        // when
        final MusicRequestDTO musicRequestDTO = testSubject.map(musicRequest);

        // then
        assertThat(musicRequestDTO.getTitle()).isEqualTo(title);
    }

    @Test
    public void mapImageUrl() {
        // given
        final MusicRequest musicRequest = new MusicRequest();
        final String imageUrl = "imageUrl";
        musicRequest.setImageUrl(imageUrl);
        musicRequest.setVotes(Collections.emptyList());

        // when
        final MusicRequestDTO musicRequestDTO = testSubject.map(musicRequest);

        // then
        assertThat(musicRequestDTO.getImageUrl()).isEqualTo(imageUrl);
    }

    @Test
    public void mapImageWidth() {
        // given
        final MusicRequest musicRequest = new MusicRequest();
        final Integer imageWidth = 64;
        musicRequest.setImageWidth(imageWidth);
        musicRequest.setVotes(Collections.emptyList());

        // when
        final MusicRequestDTO musicRequestDTO = testSubject.map(musicRequest);

        // then
        assertThat(musicRequestDTO.getImageWidth()).isEqualTo(imageWidth);
    }

    @Test
    public void mapImageHeight() {
        // given
        final MusicRequest musicRequest = new MusicRequest();
        final Integer imageHeight = 64;
        musicRequest.setImageHeight(imageHeight);
        musicRequest.setVotes(Collections.emptyList());

        // when
        final MusicRequestDTO musicRequestDTO = testSubject.map(musicRequest);

        // then
        assertThat(musicRequestDTO.getImageHeight()).isEqualTo(imageHeight);
    }

    @Test
    public void mapDuration() {
        // given
        final MusicRequest musicRequest = new MusicRequest();
        final Integer duration = 64;
        musicRequest.setDuration(duration);
        musicRequest.setVotes(Collections.emptyList());

        // when
        final MusicRequestDTO musicRequestDTO = testSubject.map(musicRequest);

        // then
        assertThat(musicRequestDTO.getDuration()).isEqualTo(duration);
    }

    @Test
    public void mapAllVotes() {
        // given
        final MusicRequest musicRequest = new MusicRequest();
        final List<Vote> votes = Arrays.asList(new Vote(), new Vote());
        musicRequest.setVotes(votes);

        // when
        final MusicRequestDTO musicRequestDTO = testSubject.map(musicRequest);

        // then
        assertThat(musicRequestDTO.getAllVotes()).isEqualTo(2L);
    }
}