package sh.stern.cobralist.search.music.request.usecases;

import org.junit.Before;
import org.junit.Test;
import sh.stern.cobralist.party.creation.domain.TrackDTO;
import sh.stern.cobralist.search.music.request.domain.SearchMusicRequestDTO;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TrackDTOToSearchMusicRequestDTOMapperTest {

    private TrackDTOToSearchMusicRequestDTOMapper testSubject;

    @Before
    public void setUp() {
        testSubject = new TrackDTOToSearchMusicRequestDTOMapper();
    }

    @Test
    public void mapId() {
        // given
        final TrackDTO trackDTO = new TrackDTO();
        final String id = "id";
        trackDTO.setStreamingId(id);

        // when
        final SearchMusicRequestDTO mappedDTO = testSubject.map(trackDTO);

        // then
        assertThat(mappedDTO.getTrackId()).isEqualTo(id);
    }

    @Test
    public void mapArtists() {
        // given
        final TrackDTO trackDTO = new TrackDTO();
        final List<String> artists = Collections.singletonList("artist");
        trackDTO.setArtists(artists);

        // when
        final SearchMusicRequestDTO mappedDTO = testSubject.map(trackDTO);

        // then
        assertThat(mappedDTO.getArtists()).isEqualTo(artists);
    }

    @Test
    public void mapUri() {
        // given
        final TrackDTO trackDTO = new TrackDTO();
        final String uri = "uri";
        trackDTO.setUri(uri);

        // when
        final SearchMusicRequestDTO mappedDTO = testSubject.map(trackDTO);

        // then
        assertThat(mappedDTO.getUri()).isEqualTo(uri);
    }

    @Test
    public void mapName() {
        // given
        final TrackDTO trackDTO = new TrackDTO();
        final String name = "name";
        trackDTO.setName(name);

        // when
        final SearchMusicRequestDTO mappedDTO = testSubject.map(trackDTO);

        // then
        assertThat(mappedDTO.getName()).isEqualTo(name);
    }

    @Test
    public void mapAlbumName() {
        // given
        final TrackDTO trackDTO = new TrackDTO();
        final String albumName = "albumName";
        trackDTO.setAlbumName(albumName);

        // when
        final SearchMusicRequestDTO mappedDTO = testSubject.map(trackDTO);

        // then
        assertThat(mappedDTO.getAlbumName()).isEqualTo(albumName);
    }

    @Test
    public void mapImageUrl() {
        // given
        final TrackDTO trackDTO = new TrackDTO();
        final String imageUrl = "imageUrl";
        trackDTO.setImageUrl(imageUrl);

        // when
        final SearchMusicRequestDTO mappedDTO = testSubject.map(trackDTO);

        // then
        assertThat(mappedDTO.getImageUrl()).isEqualTo(imageUrl);
    }

    @Test
    public void mapImageWidth() {
        // given
        final TrackDTO trackDTO = new TrackDTO();
        final int imageWidth = 64;
        trackDTO.setImageWidth(imageWidth);

        // when
        final SearchMusicRequestDTO mappedDTO = testSubject.map(trackDTO);

        // then
        assertThat(mappedDTO.getImageWidth()).isEqualTo(imageWidth);
    }

    @Test
    public void mapImageHeight() {
        // given
        final TrackDTO trackDTO = new TrackDTO();
        final int imageHeight = 65;
        trackDTO.setImageHeight(imageHeight);

        // when
        final SearchMusicRequestDTO mappedDTO = testSubject.map(trackDTO);

        // then
        assertThat(mappedDTO.getImageHeight()).isEqualTo(imageHeight);
    }

    @Test
    public void mapDuration() {
        // given
        final TrackDTO trackDTO = new TrackDTO();
        final int duration = 6545;
        trackDTO.setDuration(duration);

        // when
        final SearchMusicRequestDTO mappedDTO = testSubject.map(trackDTO);

        // then
        assertThat(mappedDTO.getDuration()).isEqualTo(duration);
    }
}