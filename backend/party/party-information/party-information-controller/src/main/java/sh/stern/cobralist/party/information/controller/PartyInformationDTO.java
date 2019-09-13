package sh.stern.cobralist.party.information.controller;

import sh.stern.cobralist.party.current.track.domain.CurrentTrackDTO;

public class PartyInformationDTO {
    private CurrentTrackDTO currentTrackDTO;

    public CurrentTrackDTO getCurrentTrackDTO() {
        return currentTrackDTO;
    }

    public void setCurrentTrackDTO(CurrentTrackDTO currentTrackDTO) {
        this.currentTrackDTO = currentTrackDTO;
    }
}
