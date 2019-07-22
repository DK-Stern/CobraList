package sh.stern.cobralist.party.joinparty.dataaccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh.stern.cobralist.guest.Guest;
import sh.stern.cobralist.guest.GuestRepository;
import sh.stern.cobralist.party.joinparty.api.JoinPartyDTO;
import sh.stern.cobralist.party.joinparty.dataaccess.api.GuestCreatedDTO;
import sh.stern.cobralist.party.joinparty.dataaccess.api.JoinPartyDataService;
import sh.stern.cobralist.party.persistence.Party;
import sh.stern.cobralist.party.persistence.PartyRepository;
import sh.stern.cobralist.security.ResourceNotFoundException;

@Service
public class JoinPartyPublicApiDataService implements JoinPartyDataService {

    private final GuestRepository guestRepository;
    private final PartyRepository partyRepository;

    @Autowired
    public JoinPartyPublicApiDataService(GuestRepository guestRepository,
                                         PartyRepository partyRepository) {
        this.guestRepository = guestRepository;
        this.partyRepository = partyRepository;
    }

    @Override
    @Transactional
    public GuestCreatedDTO createGuest(JoinPartyDTO joinPartyDto) {
        final Party party = partyRepository.findById(joinPartyDto.getPartyId())
                .orElseThrow(() -> new ResourceNotFoundException("Party", "id", joinPartyDto.getPartyId()));

        final Guest guest = new Guest();
        guest.setName(joinPartyDto.getGuestName());
        guest.setParty(party);

        final Guest savedGuest = guestRepository.saveAndFlush(guest);

        final GuestCreatedDTO guestCreatedDTO = new GuestCreatedDTO();
        guestCreatedDTO.setId(savedGuest.getId());
        guestCreatedDTO.setName(savedGuest.getName());

        return guestCreatedDTO;
    }
}
