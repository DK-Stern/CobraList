package sh.stern.cobralist.party.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sh.stern.cobralist.party.crud.api.PartyCRUDService;
import sh.stern.cobralist.party.crud.api.PartyCreationDTO;
import sh.stern.cobralist.security.CurrentUser;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

@RestController
@RequestMapping("/api/party")
public class PartyController {

    private final PartyCRUDService partyService;

    @Autowired
    public PartyController(PartyCRUDService partyService) {
        this.partyService = partyService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public ResponseEntity<PartyCreationDTO> createParty(@CurrentUser UserPrincipal userPrincipal,
                                                        @RequestBody sh.stern.cobralist.party.crud.api.PartyCreationDTO partyRequest) {
        return ResponseEntity.ok(partyService.createParty(partyRequest, userPrincipal.getId()));
    }

    @PreAuthorize("hasAnyRole('USER','GUEST')")
    @GetMapping("/{partyCode}")
    public ResponseEntity<PartyCreationDTO> getParty(@PathVariable String partyCode) {
        return ResponseEntity.ok(partyService.getParty(partyCode));
    }
}
