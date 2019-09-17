package sh.stern.cobralist.party.information.domain;

import sh.stern.cobralist.party.current.track.domain.CurrentPlaybackDTO;
import sh.stern.cobralist.party.music.requests.MusicRequestDTO;

import java.util.List;

public class PartyInformationDTO {
    private CurrentPlaybackDTO currentPlayback;
    private List<MusicRequestDTO> musicRequests;

    public CurrentPlaybackDTO getCurrentPlayback() {
        return currentPlayback;
    }

    public void setCurrentPlayback(CurrentPlaybackDTO currentPlayback) {
        this.currentPlayback = currentPlayback;
    }

    public List<MusicRequestDTO> getMusicRequests() {
        return musicRequests;
    }

    public void setMusicRequests(List<MusicRequestDTO> musicRequests) {
        this.musicRequests = musicRequests;
    }
}
