package sh.stern.cobralist.party.information.domain;

import sh.stern.cobralist.party.current.track.domain.CurrentPlaybackDTO;
import sh.stern.cobralist.party.music.requests.MusicRequestDTO;

import java.util.List;

public class PartyInformationDTO {
    private String partyCode;
    private CurrentPlaybackDTO currentPlayback;
    private List<MusicRequestDTO> musicRequests;

    public String getPartyCode() {
        return partyCode;
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
    }

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
