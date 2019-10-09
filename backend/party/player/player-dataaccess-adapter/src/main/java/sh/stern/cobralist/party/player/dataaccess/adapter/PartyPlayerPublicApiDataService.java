package sh.stern.cobralist.party.player.dataaccess.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh.stern.cobralist.party.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.party.persistence.domain.Party;
import sh.stern.cobralist.party.persistence.domain.Playlist;
import sh.stern.cobralist.party.persistence.exceptions.PartyNotFoundException;
import sh.stern.cobralist.party.player.dataaccess.port.PartyPlayerDataService;

@Service
public class PartyPlayerPublicApiDataService implements PartyPlayerDataService {

    private final PartyRepository partyRepository;

    @Autowired
    public PartyPlayerPublicApiDataService(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    @Override
    public String getPlaylistStreamingId(String partyCode) {
        return partyRepository.findByPartyCode(partyCode)
                .map(Party::getPlaylist)
                .map(Playlist::getPlaylistId)
                .orElseThrow(() -> new PartyNotFoundException(partyCode));
    }

    @Override
    @Transactional
    public void setPartyStatus(String partyCode, Boolean active) {
        final Party party = partyRepository.findByPartyCode(partyCode)
                .orElseThrow(() -> new PartyNotFoundException(partyCode));
        party.setActive(active);
        partyRepository.saveAndFlush(party);
    }
}
