package sh.stern.cobralist.party.join.api;

public class PartyJoinedDTO {
    private String token;
    private String partyCode;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPartyCode() {
        return partyCode;
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
    }
}
