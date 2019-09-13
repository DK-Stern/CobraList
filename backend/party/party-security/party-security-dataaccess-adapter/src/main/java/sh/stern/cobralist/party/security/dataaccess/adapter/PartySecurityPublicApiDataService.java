package sh.stern.cobralist.party.security.dataaccess.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.party.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.party.persistence.domain.Party;
import sh.stern.cobralist.party.persistence.domain.User;
import sh.stern.cobralist.party.persistence.exceptions.PartyNotFoundException;
import sh.stern.cobralist.party.security.dataaccess.port.PartySecurityDataService;

@Service
public class PartySecurityPublicApiDataService implements PartySecurityDataService {

    private final PartyRepository partyRepository;

    @Autowired
    public PartySecurityPublicApiDataService(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    @Override
    public boolean isUserPartyParticipant(Long userId, String partyCode) {
        final Party party = partyRepository.findByPartyCode(partyCode)
                .orElseThrow(() -> new PartyNotFoundException(partyCode));

        final User user = party.getUser();
        return user.getId().equals(userId);
    }

    @Override
    public boolean isGuestPartyParticipant(Long guestId, String partyCode) {
        final Party party = partyRepository.findByPartyCode(partyCode)
                .orElseThrow(() -> new PartyNotFoundException(partyCode));

        return (party.getGuests().stream().filter(guest -> guest.getId().equals(guestId)).count() == 1);
    }
}
