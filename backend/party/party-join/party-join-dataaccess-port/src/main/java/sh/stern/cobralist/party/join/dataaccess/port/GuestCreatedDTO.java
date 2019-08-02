package sh.stern.cobralist.party.join.dataaccess.port;

public class GuestCreatedDTO {
    private Long partyId;
    private String name;

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
