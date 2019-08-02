package sh.stern.cobralist.party.join.api;

public class JoinPartyDTO {

    private Long partyId;
    private String partyPassword;
    private String guestName;

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public String getPartyPassword() {
        return partyPassword;
    }

    public void setPartyPassword(String password) {
        this.partyPassword = password;
    }
}
