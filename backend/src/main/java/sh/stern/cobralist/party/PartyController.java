package sh.stern.cobralist.party;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sh.stern.cobralist.party.persistence.Party;
import sh.stern.cobralist.party.persistence.PartyRepository;
import sh.stern.cobralist.security.CurrentUser;
import sh.stern.cobralist.security.ResourceNotFoundException;
import sh.stern.cobralist.security.oauth2.user.UserPrincipal;

@RestController
@RequestMapping("/api/party")
public class PartyController {

    private final PartyService partyService;
    private final PartyRepository partyRepository;

    @Autowired
    public PartyController(PartyService partyService,
                           PartyRepository partyRepository) {
        this.partyService = partyService;
        this.partyRepository = partyRepository;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public ResponseEntity<PartyCreationResponse> createParty(@CurrentUser UserPrincipal userPrincipal,
                                                             @RequestBody PartyCreationDTO partyRequest) {
        Party party = partyService.createParty(partyRequest, userPrincipal.getId());

        PartyCreationResponse partyCreationResponse = new PartyCreationResponse();
        partyCreationResponse.setId(party.getId());
        partyCreationResponse.setDownVoting(party.isDownVotable());
        partyCreationResponse.setName(party.getName());
        partyCreationResponse.setPassword(party.getPassword());

        return ResponseEntity.ok(partyCreationResponse);
    }

    @PreAuthorize("hasAnyRole('USER','GUEST')")
    @GetMapping("/{partyId}")
    public ResponseEntity<PartyCreationResponse> getParty(@PathVariable Long partyId) {
        final Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new ResourceNotFoundException("Party", "id", partyId));

        PartyCreationResponse partyCreationResponse = new PartyCreationResponse();
        partyCreationResponse.setId(party.getId());
        partyCreationResponse.setDownVoting(party.isDownVotable());
        partyCreationResponse.setName(party.getName());
        partyCreationResponse.setPassword(party.getPassword());

        return ResponseEntity.ok(partyCreationResponse);
    }

    @GetMapping("/find/{partyId}")
    public ResponseEntity<FindPartyDTO> findParty(@PathVariable Long partyId) {
        final Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new ResourceNotFoundException("Party", "id", partyId));

        final FindPartyDTO findPartyDTO = new FindPartyDTO();
        findPartyDTO.setId(party.getId());
        findPartyDTO.setName(party.getName());
        findPartyDTO.setHasPassword(!party.getPassword().isBlank());

        return ResponseEntity.ok(findPartyDTO);
    }
}
