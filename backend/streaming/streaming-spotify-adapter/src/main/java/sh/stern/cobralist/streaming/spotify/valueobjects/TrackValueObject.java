package sh.stern.cobralist.streaming.spotify.valueobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonRootName(value = "track")
public class TrackValueObject {
    private String id;
    private List<String> artists;
    private String uri;
    private String name;
    private String albumName;
    private String imageUrl;
    private Integer imageHeight;
    private Integer imageWidth;

    @JsonProperty("is_local")
    private Boolean isLocal;

    @JsonProperty("album")
    private void unpackAlbumName(Map<String, Object> album) {
        this.albumName = (String) album.get("name");
        // noinspection unchecked
        final List<Map<String, Object>> images = (List<Map<String, Object>>) album.get("images");
        if (!images.isEmpty()) {
            final Map<String, Object> image = images.get(0);
            this.imageUrl = (String) image.get("url");
            this.imageWidth = (Integer) image.get("width");
            this.imageHeight = (Integer) image.get("height");
        }
    }

    @JsonProperty("artists")
    private void unpackArtists(List<Map<String, Object>> artists) {
        this.artists = artists.stream()
                .map(artist -> (String) artist.get("name"))
                .collect(Collectors.toList());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getArtists() {
        return artists;
    }

    public void setArtists(List<String> artists) {
        this.artists = artists;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(Integer imageHeight) {
        this.imageHeight = imageHeight;
    }

    public Integer getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(Integer imageWidth) {
        this.imageWidth = imageWidth;
    }

    public Boolean getLocal() {
        return isLocal;
    }

    public void setLocal(Boolean local) {
        isLocal = local;
    }
}
