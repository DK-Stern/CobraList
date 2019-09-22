package sh.stern.cobralist.streaming.spotify.valueobjects;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SearchTrackValueObject {
    private String href;

    private List<TrackValueObject> items;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public List<TrackValueObject> getItems() {
        return items;
    }

    public void setItems(List<TrackValueObject> items) {
        this.items = items;
    }
}
