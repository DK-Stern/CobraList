package sh.stern.cobralist.streaming.spotify.mapper;

import org.junit.Before;
import org.junit.Test;
import sh.stern.cobralist.party.creation.domain.TrackDTO;
import sh.stern.cobralist.streaming.spotify.valueobjects.TrackValueObject;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class TrackValueObjectToTrackDTOMapperTest {

    private TrackValueObjectToTrackDTOMapper testSubject;

    @Before
    public void setUp() {
        testSubject = new TrackValueObjectToTrackDTOMapper();
    }

    @Test
    public void mapId() {
        // given
        final String expectedId = "id";
        final TrackValueObject track = new TrackValueObject();
        track.setId(expectedId);

        // when
        final TrackDTO resultedDTO = testSubject.map(track);

        // then
        assertThat(resultedDTO.getId()).isEqualTo(expectedId);
    }

    @Test
    public void mapArtists() {
        // given
        final String firstArtist = "artist1";
        final String secondArtist = "artist2";
        final TrackValueObject track = new TrackValueObject();
        track.setArtists(Arrays.asList(firstArtist, secondArtist));

        // when
        final TrackDTO resultedDTO = testSubject.map(track);

        // then
        assertThat(resultedDTO.getArtists()).containsExactly(firstArtist, secondArtist);
    }

    @Test
    public void mapUri() {
        // given
        final String expectedUri = "uri";
        final TrackValueObject track = new TrackValueObject();
        track.setUri(expectedUri);

        // when
        final TrackDTO resultedDTO = testSubject.map(track);

        // then
        assertThat(resultedDTO.getUri()).isEqualTo(expectedUri);
    }

    @Test
    public void mapName() {
        // given
        final String expectedName = "name";
        final TrackValueObject track = new TrackValueObject();
        track.setName(expectedName);

        // when
        final TrackDTO resultedDTO = testSubject.map(track);

        // then
        assertThat(resultedDTO.getName()).isEqualTo(expectedName);
    }

    @Test
    public void mapAlbumName() {
        // given
        final String expectedAlbumName = "album";
        final TrackValueObject track = new TrackValueObject();
        track.setAlbumName(expectedAlbumName);

        // when
        final TrackDTO resultedDTO = testSubject.map(track);

        // then
        assertThat(resultedDTO.getAlbumName()).isEqualTo(expectedAlbumName);
    }

    @Test
    public void mapImageUrl() {
        // given
        final String expectedImageUrl = "imageurl";
        final TrackValueObject track = new TrackValueObject();
        track.setImageUrl(expectedImageUrl);

        // when
        final TrackDTO resultedDTO = testSubject.map(track);

        // then
        assertThat(resultedDTO.getImageUrl()).isEqualTo(expectedImageUrl);
    }

    @Test
    public void mapImageWidth() {
        // given
        final Integer expectedImageWidth = 64;
        final TrackValueObject track = new TrackValueObject();
        track.setImageWidth(expectedImageWidth);

        // when
        final TrackDTO resultedDTO = testSubject.map(track);

        // then
        assertThat(resultedDTO.getImageWidth()).isEqualTo(expectedImageWidth);
    }

    @Test
    public void mapImageHeight() {
        // given
        final Integer expectedImageHeight = 64;
        final TrackValueObject track = new TrackValueObject();
        track.setImageHeight(expectedImageHeight);

        // when
        final TrackDTO resultedDTO = testSubject.map(track);

        // then
        assertThat(resultedDTO.getImageHeight()).isEqualTo(expectedImageHeight);
    }

    @Test
    public void mapDuration() {
        // given
        final Integer expectedDuration = 640000;
        final TrackValueObject track = new TrackValueObject();
        track.setDurationMs(expectedDuration);

        // when
        final TrackDTO resultedDTO = testSubject.map(track);

        // then
        assertThat(resultedDTO.getDuration()).isEqualTo(expectedDuration);
    }
}