package sh.stern.cobralist.search.music.request.usecases;

import org.springframework.stereotype.Component;
import sh.stern.cobralist.party.creation.domain.TrackDTO;
import sh.stern.cobralist.search.music.request.domain.SearchMusicRequestDTO;

@Component
public class TrackDTOToSearchMusicRequestDTOMapper {
    public SearchMusicRequestDTO map(TrackDTO trackDTO) {
        final SearchMusicRequestDTO searchMusicRequestDTO = new SearchMusicRequestDTO();
        searchMusicRequestDTO.setTrackId(trackDTO.getId());
        searchMusicRequestDTO.setArtists(trackDTO.getArtists());
        searchMusicRequestDTO.setUri(trackDTO.getUri());
        searchMusicRequestDTO.setName(trackDTO.getName());
        searchMusicRequestDTO.setAlbumName(trackDTO.getAlbumName());
        searchMusicRequestDTO.setImageUrl(trackDTO.getImageUrl());
        searchMusicRequestDTO.setImageWidth(trackDTO.getImageWidth());
        searchMusicRequestDTO.setImageHeight(trackDTO.getImageHeight());
        searchMusicRequestDTO.setDuration(trackDTO.getDuration());
        return searchMusicRequestDTO;
    }
}
