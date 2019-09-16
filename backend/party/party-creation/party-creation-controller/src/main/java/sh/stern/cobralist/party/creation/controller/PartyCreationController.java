package sh.stern.cobralist.party.creation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh.stern.cobralist.party.creation.api.PartyCreationRequestDTO;
import sh.stern.cobralist.party.creation.api.PartyCreationService;
import sh.stern.cobralist.party.information.domain.PartyInformationDTO;
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
    public ResponseEntity<PartyInformationDTO> createParty(@CurrentUser UserPrincipal userPrincipal,
                                                           @RequestBody PartyCreationRequestDTO partyRequest) {
        return ResponseEntity.ok(partyService.createParty(userPrincipal, partyRequest));
    }
}
