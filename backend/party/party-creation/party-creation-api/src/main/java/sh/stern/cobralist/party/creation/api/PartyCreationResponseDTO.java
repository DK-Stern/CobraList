package sh.stern.cobralist.party.creation.api;

import sh.stern.cobralist.party.creation.domain.PlaylistDTO;

public class PartyCreationResponseDTO {

    private String partyCode;

    private String partyName;

    private String password;

    private Boolean downVoting;

    private String description;

    private PlaylistDTO playlist;

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isDownVoting() {
        return downVoting;
    }

    public void setDownVoting(Boolean downVoting) {
        this.downVoting = downVoting;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPartyCode() {
        return partyCode;
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
    }

    public PlaylistDTO getPlaylist() {
        return playlist;
    }

    public void setPlaylist(PlaylistDTO playlist) {
        this.playlist = playlist;
    }
}
