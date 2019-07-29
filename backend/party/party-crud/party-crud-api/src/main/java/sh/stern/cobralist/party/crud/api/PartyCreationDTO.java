package sh.stern.cobralist.party.crud.api;

public class PartyCreationDTO {

    private String partyName;

    private String password;

    private boolean downVoting;

    private String description;

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

    public boolean isDownVoting() {
        return downVoting;
    }

    public void setDownVoting(boolean downVoting) {
        this.downVoting = downVoting;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
