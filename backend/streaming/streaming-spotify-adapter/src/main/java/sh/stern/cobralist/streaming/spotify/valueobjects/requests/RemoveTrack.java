package sh.stern.cobralist.streaming.spotify.valueobjects.requests;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.ArrayList;
import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RemoveTrack {
    private List<Track> tracks;

    public RemoveTrack(List<String> tracksUris) {
        tracks = new ArrayList<>();
        tracksUris.forEach(uri -> tracks.add(new Track(uri)));
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}
