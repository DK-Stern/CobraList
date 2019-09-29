package sh.stern.cobralist.add.music.request.api;

import sh.stern.cobralist.party.creation.domain.TrackDTO;

public class AddMusicRequestDTO {
    private String partyCode;
    private TrackDTO trackDTO;

    public String getPartyCode() {
        return partyCode;
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
    }

    public TrackDTO getTrackDTO() {
        return trackDTO;
    }

    public void setTrackDTO(TrackDTO trackDTO) {
        this.trackDTO = trackDTO;
    }
}
