package sh.stern.cobralist.party.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sh.stern.cobralist.party.crud.api.PartyCRUDService;
import sh.stern.cobralist.party.crud.api.PartyCreationDTO;
import sh.stern.cobralist.party.persistence.dataaccess.PartyRepository;
import sh.stern.cobralist.party.persistence.domain.Party;
import sh.stern.cobralist.party.persistence.exceptions.PartyNotFoundException;
import sh.stern.cobralist.security.CurrentUser;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

@RestController
@RequestMapping("/api/party")
public class PartyController {

    private final PartyCRUDService partyService;
    private final PartyRepository partyRepository;

    @Autowired
    public PartyController(PartyCRUDService partyService,
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
                .orElseThrow(() -> new PartyNotFoundException(partyId.toString()));

        PartyCreationResponse partyCreationResponse = new PartyCreationResponse();
        partyCreationResponse.setId(party.getId());
        partyCreationResponse.setDownVoting(party.isDownVotable());
        partyCreationResponse.setName(party.getName());
        partyCreationResponse.setPassword(party.getPassword());

        return ResponseEntity.ok(partyCreationResponse);
    }
}
