package sh.stern.cobralist.party.current.track.dataaccess.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh.stern.cobralist.party.current.track.dataaccess.port.CurrentTrackDataService;
import sh.stern.cobralist.party.current.track.domain.ActivePartyDTO;
import sh.stern.cobralist.party.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.party.persistence.domain.Party;
import sh.stern.cobralist.party.persistence.exceptions.PartyNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrentTrackPublicApiDataService implements CurrentTrackDataService {

    private final PartyRepository partyRepository;

    @Autowired
    public CurrentTrackPublicApiDataService(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    @Override
    public List<ActivePartyDTO> getActiveParties() {
        final List<Party> activeParties = partyRepository.findByActive(true);

        return activeParties.stream()
                .map(party -> new ActivePartyDTO(party.getPartyCode(), party.getUser().getName(), party.getUser().getEmail()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void changePartyActiveStatus(String partyCode, Boolean activeStatus) {
        final Party party = partyRepository.findByPartyCode(partyCode)
                .orElseThrow(() -> new PartyNotFoundException(partyCode));
        party.setActive(activeStatus);
        partyRepository.save(party);
    }
}
