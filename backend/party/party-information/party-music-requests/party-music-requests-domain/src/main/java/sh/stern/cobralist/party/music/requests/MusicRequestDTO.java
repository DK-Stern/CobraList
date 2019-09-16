package sh.stern.cobralist.party.music.requests;

import java.util.ArrayList;
import java.util.List;

public class MusicRequestDTO {

    private String trackId;

    private List<String> artist = new ArrayList<>();

    private String title;

    private String imageUrl;

    private Integer imageHeight;

    private Integer imageWidth;

    private Integer duration;

    private Boolean alreadyVoted;

    private Long allVotes;

    private Long downVotes;

    private Long upVotes;

    private Long rating;

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public List<String> getArtist() {
        return artist;
    }

    public void setArtist(List<String> artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean getAlreadyVoted() {
        return alreadyVoted;
    }

    public void setAlreadyVoted(Boolean alreadyVoted) {
        this.alreadyVoted = alreadyVoted;
    }

    public Long getAllVotes() {
        return allVotes;
    }

    public void setAllVotes(Long allVotes) {
        this.allVotes = allVotes;
    }

    public Long getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(Long downVotes) {
        this.downVotes = downVotes;
    }

    public Long getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(Long upVotes) {
        this.upVotes = upVotes;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }
}
