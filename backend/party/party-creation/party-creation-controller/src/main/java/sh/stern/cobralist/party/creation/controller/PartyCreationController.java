package sh.stern.cobralist.party.creation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sh.stern.cobralist.party.creation.api.PartyCreationRequestDTO;
import sh.stern.cobralist.party.creation.api.PartyCreationResponseDTO;
import sh.stern.cobralist.party.creation.api.PartyCreationService;
import sh.stern.cobralist.party.creation.domain.PartyDTO;
import sh.stern.cobralist.security.CurrentUser;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

@RestController
@RequestMapping("/api/party")
public class PartyCreationController {

    private final PartyCreationService partyService;

    @Autowired
    public PartyCreationController(PartyCreationService partyService) {
        this.partyService = partyService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public ResponseEntity<PartyCreationResponseDTO> createParty(@CurrentUser UserPrincipal userPrincipal,
                                                                @RequestBody PartyCreationRequestDTO partyRequest) {
        return ResponseEntity.ok(partyService.createParty(userPrincipal.getUsername(), userPrincipal.getId(), partyRequest));
    }

    @PreAuthorize("hasAnyRole('USER','GUEST')")
    @GetMapping("/{partyCode}")
    public ResponseEntity<PartyDTO> getParty(@PathVariable String partyCode) {
        return ResponseEntity.ok(partyService.getParty(partyCode));
    }
}
