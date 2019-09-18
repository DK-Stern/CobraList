package sh.stern.cobralist.streaming.spotify.valueobjects.requests;

public class Track {
    private String uri;

    public Track(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
