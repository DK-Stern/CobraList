package sh.stern.cobralist.vote.music.request.api;

public class VoteMusicRequestDTO {
    private Boolean isDownVote;
    private Long musicRequestId;
    private String partyCode;

    public Boolean getDownVote() {
        return isDownVote;
    }

    public void setDownVote(Boolean downVote) {
        isDownVote = downVote;
    }

    public Long getMusicRequestId() {
        return musicRequestId;
    }

    public void setMusicRequestId(Long musicRequestId) {
        this.musicRequestId = musicRequestId;
    }

    public String getPartyCode() {
        return partyCode;
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
    }
}
