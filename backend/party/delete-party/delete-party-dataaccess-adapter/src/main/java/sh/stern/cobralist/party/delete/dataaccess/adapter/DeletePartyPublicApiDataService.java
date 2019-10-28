package sh.stern.cobralist.party.delete.dataaccess.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh.stern.cobralist.party.delete.dataaccess.port.DeletePartyDataService;
import sh.stern.cobralist.party.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.party.persistence.dataaccess.PlaylistRepository;
import sh.stern.cobralist.party.persistence.domain.Party;
import sh.stern.cobralist.party.persistence.exceptions.PartyNotFoundException;

@Service
public class DeletePartyPublicApiDataService implements DeletePartyDataService {

    private final PartyRepository partyRepository;
    private final PlaylistRepository playlistRepository;

    @Autowired
    public DeletePartyPublicApiDataService(PartyRepository partyRepository,
                                           PlaylistRepository playlistRepository) {
        this.partyRepository = partyRepository;
        this.playlistRepository = playlistRepository;
    }

    @Transactional
    @Override
    public void deleteParty(String partyCode) {
        final Party party = partyRepository.findByPartyCode(partyCode)
                .orElseThrow(() -> new PartyNotFoundException(partyCode));
        playlistRepository.delete(party.getPlaylist());
        partyRepository.delete(party);
    }

    @Override
    public String getPlaylistStreamingId(String partyCode) {
        return partyRepository.findByPartyCode(partyCode).map(party -> party.getPlaylist().getPlaylistId())
                .orElseThrow(() -> new PartyNotFoundException(partyCode));
    }
}
