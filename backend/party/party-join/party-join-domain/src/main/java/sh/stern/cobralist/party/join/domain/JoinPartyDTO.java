package sh.stern.cobralist.party.join.domain;

public class JoinPartyDTO {

    private String partyCode;
    private String partyPassword;
    private String guestName;

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getPartyCode() {
        return partyCode;
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
    }

    public String getPartyPassword() {
        return partyPassword;
    }

    public void setPartyPassword(String password) {
        this.partyPassword = password;
    }
}
