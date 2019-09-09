package sh.stern.cobralist.streaming.spotify.valueobjects;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TrackValueObjectWrapper {
    private TrackValueObject track;

    public TrackValueObject getTrack() {
        return track;
    }

    public void setTrack(TrackValueObject track) {
        this.track = track;
    }
}
