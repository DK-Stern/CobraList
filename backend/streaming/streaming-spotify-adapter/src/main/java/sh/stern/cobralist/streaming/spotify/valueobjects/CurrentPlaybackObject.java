package sh.stern.cobralist.streaming.spotify.valueobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CurrentPlaybackObject {

    @JsonProperty("is_playing")
    private Boolean isPlaying;
    private Integer progressMs;
    private TrackValueObject item;

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

    public TrackValueObject getItem() {
        return item;
    }

    public void setItem(TrackValueObject item) {
        this.item = item;
    }
}
