package sh.stern.cobralist.streaming.spotify.mapper;

import org.springframework.stereotype.Component;
import sh.stern.cobralist.party.creation.domain.TrackDTO;
import sh.stern.cobralist.streaming.spotify.valueobjects.TrackValueObject;

@Component
public class TrackValueObjectToTrackDTOMapper {
    public TrackDTO map(TrackValueObject track) {
        final TrackDTO trackDTO = new TrackDTO();

        trackDTO.setId(track.getId());
        trackDTO.setArtists(track.getArtists());
        trackDTO.setName(track.getName());
        trackDTO.setAlbumName(track.getAlbumName());
        trackDTO.setUri(track.getUri());
        trackDTO.setImageUrl(track.getImageUrl());
        trackDTO.setImageWidth(track.getImageWidth());
        trackDTO.setImageHeight(track.getImageHeight());

        return trackDTO;
    }
}
