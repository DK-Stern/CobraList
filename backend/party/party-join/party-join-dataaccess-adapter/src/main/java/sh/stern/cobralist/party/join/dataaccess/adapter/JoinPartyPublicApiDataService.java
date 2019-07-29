package sh.stern.cobralist.party.join.dataaccess.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh.stern.cobralist.party.join.api.FindPartyDTO;
import sh.stern.cobralist.party.join.api.JoinPartyDTO;
import sh.stern.cobralist.party.join.dataaccess.port.GuestCreatedDTO;
import sh.stern.cobralist.party.join.dataaccess.port.JoinPartyDataService;
import sh.stern.cobralist.persistence.dataaccess.GuestRepository;
import sh.stern.cobralist.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.persistence.domain.Guest;
import sh.stern.cobralist.persistence.domain.Party;
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

    @Override
    public FindPartyDTO findParty(Long partyId) {
        final Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new ResourceNotFoundException("Party", "id", partyId));

        final FindPartyDTO findPartyDTO = new FindPartyDTO();
        findPartyDTO.setId(party.getId());
        findPartyDTO.setName(party.getName());
        findPartyDTO.setHasPassword(!party.getPassword().isBlank());

        return findPartyDTO;
    }
}
