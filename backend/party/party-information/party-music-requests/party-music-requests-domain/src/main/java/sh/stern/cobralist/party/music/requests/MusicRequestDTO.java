package sh.stern.cobralist.party.music.requests;

import java.util.ArrayList;
import java.util.List;

public class MusicRequestDTO {

    private Long musicRequestId;

    private Integer position;

    private List<String> artist = new ArrayList<>();

    private String title;

    private String imageUrl;

    private Integer imageHeight;

    private Integer imageWidth;

    private Integer duration;

    private Boolean alreadyVoted;

    private Integer allVotes;

    private Integer downVotes;

    private Integer upVotes;

    private Integer rating;

    public Long getMusicRequestId() {
        return musicRequestId;
    }

    public void setMusicRequestId(Long musicRequestId) {
        this.musicRequestId = musicRequestId;
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

    public Integer getAllVotes() {
        return allVotes;
    }

    public void setAllVotes(Integer allVotes) {
        this.allVotes = allVotes;
    }

    public Integer getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(Integer downVotes) {
        this.downVotes = downVotes;
    }

    public Integer getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(Integer upVotes) {
        this.upVotes = upVotes;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
