package sh.stern.cobralist.party;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.party.persistence.Party;
import sh.stern.cobralist.party.persistence.PartyRepository;
import sh.stern.cobralist.security.ResourceNotFoundException;
import sh.stern.cobralist.security.oauth2.user.model.User;
import sh.stern.cobralist.security.oauth2.user.repository.UserRepository;

import java.util.Date;

@Service
public class PartyService {

    private final UserRepository userRepository;
    private final PartyRepository partyRepository;

    @Autowired
    public PartyService(UserRepository userRepository,
                        PartyRepository partyRepository) {
        this.userRepository = userRepository;
        this.partyRepository = partyRepository;
    }

    public Party createParty(PartyCreationDTO partyDTO, Long userId) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // todo erstelle playlist

        // todo party dto vorher pruefen
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
