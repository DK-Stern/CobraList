package sh.stern.cobralist.party.join.dataaccess.adapter;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh.stern.cobralist.party.join.dataaccess.port.GuestCreatedDTO;
import sh.stern.cobralist.party.join.dataaccess.port.JoinPartyDataService;
import sh.stern.cobralist.party.join.domain.FindPartyDTO;
import sh.stern.cobralist.party.join.domain.JoinPartyDTO;
import sh.stern.cobralist.party.persistence.dataaccess.GuestRepository;
import sh.stern.cobralist.party.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.party.persistence.domain.Guest;
import sh.stern.cobralist.party.persistence.domain.Party;
import sh.stern.cobralist.party.persistence.exceptions.PartyNotFoundException;

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
        final Party party = partyRepository.findByPartyCode(joinPartyDto.getPartyCode())
                .orElseThrow(() -> new PartyNotFoundException(joinPartyDto.getPartyCode()));

        final Guest guest = new Guest();
        guest.setName(joinPartyDto.getGuestName());
        guest.setParty(party);

        final Guest savedGuest = guestRepository.saveAndFlush(guest);

        final GuestCreatedDTO guestCreatedDTO = new GuestCreatedDTO();
        guestCreatedDTO.setGuestId(savedGuest.getId());
        guestCreatedDTO.setPartyCode(party.getPartyCode());
        guestCreatedDTO.setName(savedGuest.getName());

        return guestCreatedDTO;
    }

    @Override
    public FindPartyDTO findParty(String partyCode) {
        final Party party = partyRepository.findByPartyCode(partyCode)
                .orElseThrow(() -> new PartyNotFoundException(partyCode));

        final FindPartyDTO findPartyDTO = new FindPartyDTO();
        findPartyDTO.setPartyCode(party.getPartyCode());
        findPartyDTO.setName(party.getName());
        findPartyDTO.setHasPassword(!Strings.isBlank(party.getPassword()));

        return findPartyDTO;
    }

    @Override
    public Long countGuestName(String guestName, String partyCode) {
        final Party party = partyRepository.findByPartyCode(partyCode)
                .orElseThrow(() -> new PartyNotFoundException(partyCode));
        return guestRepository.countGuestByNameAndParty(guestName, party);
    }

    @Override
    public String getPartyPassword(String partyCode) {
        final Party party = partyRepository.findByPartyCode(partyCode)
                .orElseThrow(() -> new PartyNotFoundException(partyCode));

        return party.getPassword();
    }
}
