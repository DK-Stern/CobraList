package sh.stern.cobralist.party.crud.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.party.crud.api.PartyCRUDService;
import sh.stern.cobralist.party.crud.api.PartyCreationDTO;
import sh.stern.cobralist.party.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.party.persistence.dataaccess.UserRepository;
import sh.stern.cobralist.party.persistence.domain.Party;
import sh.stern.cobralist.party.persistence.domain.User;
import sh.stern.cobralist.party.persistence.exceptions.PartyNotFoundException;
import sh.stern.cobralist.party.persistence.exceptions.UserNotFoundException;

import java.util.Date;

@Service
public class PartyCRUDPublicApiService implements PartyCRUDService {

    private final UserRepository userRepository;
    private final PartyRepository partyRepository;

    @Autowired
    public PartyCRUDPublicApiService(UserRepository userRepository,
                                     PartyRepository partyRepository) {
        this.userRepository = userRepository;
        this.partyRepository = partyRepository;
    }

    public PartyCreationDTO createParty(PartyCreationDTO partyDTO, Long userId) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // todo erstelle playlist

        // todo party dto vorher pruefen

        // todo basisplaylist

        Party party = new Party();
        party.setUser(user);
        party.setCreationDate(new Date());
        party.setName(partyDTO.getPartyName());
        party.setPassword(partyDTO.getPassword());
        party.setDownVotable(partyDTO.isDownVoting());
        party.setDescription(partyDTO.getDescription());

        party = partyRepository.save(party);

        PartyCreationDTO partyCreationDTO = new PartyCreationDTO();
        partyCreationDTO.setPartyId(party.getId().toString());
        partyCreationDTO.setDownVoting(party.isDownVotable());
        partyCreationDTO.setPartyName(party.getName());
        partyCreationDTO.setPassword(party.getPassword());
        partyCreationDTO.setDescription(party.getDescription());

        return partyCreationDTO;
    }

    @Override
    public PartyCreationDTO getParty(Long partyId) {
        final Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new PartyNotFoundException(partyId.toString()));

        PartyCreationDTO partyCreationDTO = new PartyCreationDTO();
        partyCreationDTO.setPartyId(party.getId().toString());
        partyCreationDTO.setDownVoting(party.isDownVotable());
        partyCreationDTO.setPartyName(party.getName());
        partyCreationDTO.setPassword(party.getPassword());

        return partyCreationDTO;
    }
}
