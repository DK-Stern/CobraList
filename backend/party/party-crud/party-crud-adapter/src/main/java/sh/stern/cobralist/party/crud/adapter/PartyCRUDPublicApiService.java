package sh.stern.cobralist.party.crud.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.party.crud.api.PartyCRUDService;
import sh.stern.cobralist.party.crud.api.PartyCreationDTO;
import sh.stern.cobralist.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.persistence.dataaccess.UserRepository;
import sh.stern.cobralist.persistence.domain.Party;
import sh.stern.cobralist.persistence.domain.User;

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

    public Party createParty(PartyCreationDTO partyDTO, Long userId) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Benutzer mit der Id '" + userId + "' konnte nicht gefunden werden."));

        // todo erstelle playlist

        // todo party dto vorher pruefen

        // todo basisplaylist

        final Party party = new Party();
        party.setUser(user);
        party.setCreationDate(new Date());
        party.setName(partyDTO.getPartyName());
        party.setPassword(partyDTO.getPassword());
        party.setDownVotable(partyDTO.isDownVoting());
        party.setDescription(partyDTO.getDescription());

        return partyRepository.save(party);
    }
}
