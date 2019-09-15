package sh.stern.cobralist.party.current.track.domain;

import sh.stern.cobralist.party.creation.domain.TrackDTO;

public class CurrentPlaybackDTO {
    private Boolean isPlaying;
    private Integer progressMs;
    private TrackDTO currentTrack = new TrackDTO();

    public Boolean getPlaying() {
        return isPlaying;
    }

    public void setPlaying(Boolean playing) {
        isPlaying = playing;
    }

    public Integer getProgressMs() {
        return progressMs;
    }

    public void setProgressMs(Integer progressMs) {
        this.progressMs = progressMs;
    }

    public TrackDTO getCurrentTrack() {
        return currentTrack;
    }

    public void setCurrentTrack(TrackDTO currentTrack) {
        this.currentTrack = currentTrack;
    }
}
