package sh.stern.cobralist.streaming.spotify.valueobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PlaylistObject {

    private Boolean collaborative;

    private String description;

    private ExternalUrlObject externalUrls;

    private FollowersObject followersObject;

    private String href;

    private String id;

    private ImageObject[] images;

    private String name;

    private UserObject owner;

    @JsonProperty("public")
    private Boolean isPublic;

    private String snapshotId;

    private PagingObject<PlaylistTracksObject> tracks;

    private String type;

    private String uri;

    public Boolean getCollaborative() {
        return collaborative;
    }

    public void setCollaborative(Boolean collaborative) {
        this.collaborative = collaborative;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExternalUrlObject getExternalUrls() {
        return externalUrls;
    }

    public void setExternalUrls(ExternalUrlObject externalUrls) {
        this.externalUrls = externalUrls;
    }

    public FollowersObject getFollowersObject() {
        return followersObject;
    }

    public void setFollowersObject(FollowersObject followersObject) {
        this.followersObject = followersObject;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ImageObject[] getImages() {
        return images;
    }

    public void setImages(ImageObject[] images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserObject getOwner() {
        return owner;
    }

    public void setOwner(UserObject owner) {
        this.owner = owner;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public PagingObject<PlaylistTracksObject> getTracks() {
        return tracks;
    }

    public void setTracks(PagingObject<PlaylistTracksObject> tracks) {
        this.tracks = tracks;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
