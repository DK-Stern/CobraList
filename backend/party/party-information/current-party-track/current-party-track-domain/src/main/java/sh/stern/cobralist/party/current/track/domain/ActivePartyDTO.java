package sh.stern.cobralist.party.current.track.domain;

public class ActivePartyDTO {
    private String partyCode;
    private String creatorName;
    private String creatorEmail;
    private Long playlistId;

    public ActivePartyDTO() {
    }

    public ActivePartyDTO(String partyCode, String creatorName, String creatorEmail, Long playlistId) {
        this.partyCode = partyCode;
        this.creatorName = creatorName;
        this.creatorEmail = creatorEmail;
        this.playlistId = playlistId;
    }

    public String getPartyCode() {
        return partyCode;
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public Long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Long playlistId) {
        this.playlistId = playlistId;
    }
}
