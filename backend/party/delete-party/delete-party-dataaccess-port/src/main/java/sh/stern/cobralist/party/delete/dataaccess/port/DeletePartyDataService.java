package sh.stern.cobralist.party.delete.dataaccess.port;

public interface DeletePartyDataService {
    void deleteParty(String partyCode);

    String getPlaylistStreamingId(String partyCode);
}
