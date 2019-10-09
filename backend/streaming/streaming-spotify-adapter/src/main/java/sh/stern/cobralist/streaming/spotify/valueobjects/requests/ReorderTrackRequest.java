package sh.stern.cobralist.streaming.spotify.valueobjects.requests;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ReorderTrackRequest {

    private Integer rangeStart;

    private Integer insertBefore;

    public Integer getRangeStart() {
        return rangeStart;
    }

    public void setRangeStart(Integer rangeStart) {
        this.rangeStart = rangeStart;
    }

    public Integer getInsertBefore() {
        return insertBefore;
    }

    public void setInsertBefore(Integer insertBefore) {
        this.insertBefore = insertBefore;
    }
}
