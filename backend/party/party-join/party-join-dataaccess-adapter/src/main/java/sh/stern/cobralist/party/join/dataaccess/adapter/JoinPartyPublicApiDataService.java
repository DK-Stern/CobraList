package sh.stern.cobralist.party.join.dataaccess.adapter;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh.stern.cobralist.party.join.api.FindPartyDTO;
import sh.stern.cobralist.party.join.api.JoinPartyDTO;
import sh.stern.cobralist.party.join.dataaccess.port.GuestCreatedDTO;
import sh.stern.cobralist.party.join.dataaccess.port.JoinPartyDataService;
import sh.stern.cobralist.party.persistence.dataaccess.GuestRepository;
import sh.stern.cobralist.party.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.party.persistence.domain.Guest;
import sh.stern.cobralist.party.persistence.domain.Party;
import sh.stern.cobralist.party.persistence.exceptions.PartyNotFoundException;

@Service
public class JoinPartyPublicApiDataService implements JoinPartyDataService {

    public static final String PARTY_NOT_FOUND_ERROR_MESSAGE = "Party mit der ID ''{0,number,#}'' konnte nicht gefunden werden.";
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
                .orElseThrow(() -> new PartyNotFoundException(joinPartyDto.getPartyId().toString()));

        final Guest guest = new Guest();
        guest.setName(joinPartyDto.getGuestName());
        guest.setParty(party);

        final Guest savedGuest = guestRepository.saveAndFlush(guest);

        final GuestCreatedDTO guestCreatedDTO = new GuestCreatedDTO();
        guestCreatedDTO.setPartyId(party.getId());
        guestCreatedDTO.setName(savedGuest.getName());

        return guestCreatedDTO;
    }

    @Override
    public FindPartyDTO findParty(Long partyId) {
        final Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new PartyNotFoundException(partyId.toString()));

        final FindPartyDTO findPartyDTO = new FindPartyDTO();
        findPartyDTO.setId(party.getId());
        findPartyDTO.setName(party.getName());
        findPartyDTO.setHasPassword(!Strings.isBlank(party.getPassword()));

        return findPartyDTO;
    }

    @Override
    public Long countGuestName(String guestName) {
        return guestRepository.countGuestByName(guestName);
    }

    @Override
    public String getPartyPassword(Long partyId) {
        final Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new PartyNotFoundException(partyId.toString()));

        return party.getPassword();
    }
}
