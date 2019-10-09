package sh.stern.cobralist.streaming.spotify.valueobjects.requests;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class StartPlaylistRequest {
    private String contextUri;

    public String getContextUri() {
        return contextUri;
    }

    public void setContextUri(String contextUri) {
        this.contextUri = contextUri;
    }
}
