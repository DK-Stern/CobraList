package sh.stern.cobralist.streaming.spotify.valueobjects.requests;

import java.util.List;

public class AddTracksTracksToPlaylistRequest {
    private List<String> uris;

    public AddTracksTracksToPlaylistRequest(List<String> uris) {
        this.uris = uris;
    }

    public List<String> getUris() {
        return uris;
    }

    public void setUris(List<String> uris) {
        this.uris = uris;
    }
}
