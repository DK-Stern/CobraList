package sh.stern.cobralist.party.information.dataaccess.port;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.party.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.party.persistence.domain.Party;
import sh.stern.cobralist.party.persistence.exceptions.PartyNotFoundException;

@Service
public class PartyInformationPublicApiDataService implements PartyInformationDataService {

    private final PartyRepository partyRepository;

    @Autowired
    public PartyInformationPublicApiDataService(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    @Override
    public boolean isPartyDownvotable(String partyCode) {
        return partyRepository.findByPartyCode(partyCode)
                .map(Party::isDownVotable)
                .orElseThrow(() -> new PartyNotFoundException(partyCode));
    }
}
