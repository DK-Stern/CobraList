package sh.stern.cobralist.streaming.spotify.valueobjects;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SearchTrackValueObjectWrapper {
    private SearchTrackValueObject tracks;

    public SearchTrackValueObject getTracks() {
        return tracks;
    }

    public void setTracks(SearchTrackValueObject tracks) {
        this.tracks = tracks;
    }
}
