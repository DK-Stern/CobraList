package sh.stern.cobralist.add.music.request.api;

import sh.stern.cobralist.party.creation.domain.TrackDTO;

public class AddMusicRequestDTO {
    private String partyCode;
    private TrackDTO track;

    public String getPartyCode() {
        return partyCode;
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
    }

    public TrackDTO getTrack() {
        return track;
    }

    public void setTrack(TrackDTO track) {
        this.track = track;
    }
}
