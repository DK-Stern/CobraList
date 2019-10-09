package sh.stern.cobralist.position.music.request.dataaccess.port;

public class MusicRequestPositionDTO {
    private Long musicRequestId;

    private int position;

    private int rating;

    private int upVotes;

    public MusicRequestPositionDTO(Long musicRequestId, int position, int rating, int upVotes) {
        this.musicRequestId = musicRequestId;
        this.position = position;
        this.rating = rating;
        this.upVotes = upVotes;
    }

    public Long getMusicRequestId() {
        return musicRequestId;
    }

    public void setMusicRequestId(Long musicRequestId) {
        this.musicRequestId = musicRequestId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }
}
