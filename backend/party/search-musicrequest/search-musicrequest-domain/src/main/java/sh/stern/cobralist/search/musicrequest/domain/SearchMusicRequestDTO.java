package sh.stern.cobralist.search.musicrequest.domain;

import java.util.List;

public class SearchMusicRequestDTO {
    private boolean isAlreadyInPlaylist;
    private String trackId;
    private List<String> artists;
    private String uri;
    private String name;
    private String albumName;
    private String imageUrl;
    private Integer imageWidth;
    private Integer imageHeight;
    private Integer duration;

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public boolean isAlreadyInPlaylist() {
        return isAlreadyInPlaylist;
    }

    public void setAlreadyInPlaylist(boolean alreadyInPlaylist) {
        isAlreadyInPlaylist = alreadyInPlaylist;
    }

    public List<String> getArtists() {
        return artists;
    }

    public void setArtists(List<String> artists) {
        this.artists = artists;
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

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(Integer imageWidth) {
        this.imageWidth = imageWidth;
    }

    public Integer getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(Integer imageHeight) {
        this.imageHeight = imageHeight;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
