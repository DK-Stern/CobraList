package sh.stern.cobralist.party.music.requests;

import org.junit.Before;
import org.junit.Test;
import sh.stern.cobralist.party.persistence.domain.MusicRequest;

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
        musicRequest.setUpVotes(1);
        musicRequest.setDownVotes(1);

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
        musicRequest.setUpVotes(1);
        musicRequest.setDownVotes(1);

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
        musicRequest.setUpVotes(1);
        musicRequest.setDownVotes(1);

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
        musicRequest.setUpVotes(1);
        musicRequest.setDownVotes(1);

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
        musicRequest.setUpVotes(1);
        musicRequest.setDownVotes(1);

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
        musicRequest.setUpVotes(1);
        musicRequest.setDownVotes(1);

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
        musicRequest.setUpVotes(1);
        musicRequest.setDownVotes(1);

        // when
        final MusicRequestDTO musicRequestDTO = testSubject.map(musicRequest);

        // then
        assertThat(musicRequestDTO.getDuration()).isEqualTo(duration);
    }

    @Test
    public void mapPosition() {
        // given
        final MusicRequest musicRequest = new MusicRequest();
        final int position = 5;
        musicRequest.setPosition(position);
        musicRequest.setUpVotes(1);
        musicRequest.setDownVotes(1);

        // when
        final MusicRequestDTO musicRequestDTO = testSubject.map(musicRequest);

        // then
        assertThat(musicRequestDTO.getPosition()).isEqualTo(position);
    }

    @Test
    public void mapUpVotes() {
        // given
        final MusicRequest musicRequest = new MusicRequest();
        final int upVotes = 5;
        musicRequest.setUpVotes(upVotes);
        musicRequest.setDownVotes(1);

        // when
        final MusicRequestDTO musicRequestDTO = testSubject.map(musicRequest);

        // then
        assertThat(musicRequestDTO.getUpVotes()).isEqualTo(upVotes);
    }

    @Test
    public void mapDownVotes() {
        // given
        final MusicRequest musicRequest = new MusicRequest();
        final int downVotes = 5;
        musicRequest.setDownVotes(downVotes);
        musicRequest.setUpVotes(1);

        // when
        final MusicRequestDTO musicRequestDTO = testSubject.map(musicRequest);

        // then
        assertThat(musicRequestDTO.getDownVotes()).isEqualTo(downVotes);
    }

    @Test
    public void mapAllVotes() {
        // given
        final MusicRequest musicRequest = new MusicRequest();
        final int upVotes = 4;
        musicRequest.setUpVotes(upVotes);
        final int downVotes = 5;
        musicRequest.setDownVotes(downVotes);

        // when
        final MusicRequestDTO musicRequestDTO = testSubject.map(musicRequest);

        // then
        assertThat(musicRequestDTO.getAllVotes()).isEqualTo(upVotes + downVotes);
    }

    @Test
    public void mapRating() {
        // given
        final MusicRequest musicRequest = new MusicRequest();
        final int rating = 4;
        musicRequest.setUpVotes(5);
        musicRequest.setDownVotes(1);
        musicRequest.setRating(rating);

        // when
        final MusicRequestDTO musicRequestDTO = testSubject.map(musicRequest);

        // then
        assertThat(musicRequestDTO.getRating()).isEqualTo(rating);
    }
}