package sh.stern.cobralist.party.music.requests;

import org.springframework.stereotype.Component;
import sh.stern.cobralist.party.persistence.domain.MusicRequest;

@Component
public class MusicRequestToMusicRequestDTOMapper {
    public MusicRequestDTO map(MusicRequest musicRequest) {
        final MusicRequestDTO musicRequestDTO = new MusicRequestDTO();
        musicRequestDTO.setTrackId(musicRequest.getTrackId());
        musicRequestDTO.setArtist(musicRequest.getArtist());
        musicRequestDTO.setTitle(musicRequest.getTitle());
        musicRequestDTO.setImageUrl(musicRequest.getImageUrl());
        musicRequestDTO.setImageWidth(musicRequest.getImageWidth());
        musicRequestDTO.setImageHeight(musicRequest.getImageHeight());
        musicRequestDTO.setDuration(musicRequest.getDuration());
        musicRequestDTO.setAllVotes(musicRequest.getVotes() == null ? 0 : (long) musicRequest.getVotes().size());
        return musicRequestDTO;
    }
}
