package sh.stern.cobralist.streaming.spotify.valueobjects.requests;

import java.util.List;

public class AddTracksTracksToPlaylistRequestWithPosition {
    private List<String> uris;
    private int position;

    public AddTracksTracksToPlaylistRequestWithPosition(List<String> uris, int position) {
        this.uris = uris;
        this.position = position;
    }

    public List<String> getUris() {
        return uris;
    }

    public void setUris(List<String> uris) {
        this.uris = uris;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
