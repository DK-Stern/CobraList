package sh.stern.cobralist.party.joinparty.api;

public class PartyJoinedDTO {
    private String token;
    private Long partyId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }
}
