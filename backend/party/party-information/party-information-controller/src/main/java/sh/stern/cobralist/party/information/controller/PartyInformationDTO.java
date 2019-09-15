package sh.stern.cobralist.party.information.controller;

import sh.stern.cobralist.party.current.track.domain.CurrentPlaybackDTO;

public class PartyInformationDTO {
    private CurrentPlaybackDTO currentPlaybackDTO;

    public CurrentPlaybackDTO getCurrentPlaybackDTO() {
        return currentPlaybackDTO;
    }

    public void setCurrentPlaybackDTO(CurrentPlaybackDTO currentPlaybackDTO) {
        this.currentPlaybackDTO = currentPlaybackDTO;
    }
}
