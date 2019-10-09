package sh.stern.cobralist.party.player.dataaccess.port;

public interface PartyPlayerDataService {
    String getPlaylistStreamingId(String partyCode);

    void setPartyStatus(String partyCode, Boolean active);
}
