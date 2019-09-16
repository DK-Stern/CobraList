package sh.stern.cobralist.party.information.domain;

import sh.stern.cobralist.party.current.track.domain.CurrentPlaybackDTO;
import sh.stern.cobralist.party.music.requests.MusicRequestDTO;

import java.util.List;

public class PartyInformationDTO {
    private CurrentPlaybackDTO currentPlaybackDTO;
    private List<MusicRequestDTO> musicRequests;

    public CurrentPlaybackDTO getCurrentPlaybackDTO() {
        return currentPlaybackDTO;
    }

    public void setCurrentPlaybackDTO(CurrentPlaybackDTO currentPlaybackDTO) {
        this.currentPlaybackDTO = currentPlaybackDTO;
    }

    public List<MusicRequestDTO> getMusicRequests() {
        return musicRequests;
    }

    public void setMusicRequests(List<MusicRequestDTO> musicRequests) {
        this.musicRequests = musicRequests;
    }
}
