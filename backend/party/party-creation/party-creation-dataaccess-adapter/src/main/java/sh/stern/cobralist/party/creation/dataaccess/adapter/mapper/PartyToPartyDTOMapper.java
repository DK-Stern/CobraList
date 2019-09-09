package sh.stern.cobralist.party.creation.dataaccess.adapter.mapper;

import org.springframework.stereotype.Component;
import sh.stern.cobralist.party.creation.domain.PartyDTO;
import sh.stern.cobralist.party.persistence.domain.Party;

@Component
public class PartyToPartyDTOMapper {

    public PartyDTO map(Party party) {
        final PartyDTO partyDTO = new PartyDTO();

        partyDTO.setPartyCode(party.getPartyCode());
        partyDTO.setPartyName(party.getName());
        partyDTO.setDescription(party.getDescription());
        partyDTO.setDownVoting(party.isDownVotable());
        partyDTO.setPassword(party.getPassword());

        return partyDTO;
    }
}
