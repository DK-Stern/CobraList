package sh.stern.cobralist.party.information.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sh.stern.cobralist.party.security.api.PartySecurityService;
import sh.stern.cobralist.security.CurrentUser;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

@RestController
@RequestMapping("/api/party")
public class PartyInformationController {

    private final PartySecurityService partySecurityService;

    @Autowired
    public PartyInformationController(
            PartySecurityService partySecurityService
    ) {
        this.partySecurityService = partySecurityService;
    }

    @PreAuthorize("hasAnyRole('USER','GUEST')")
    @GetMapping(path = "/currentTrack")
    public ResponseEntity<PartyInformationDTO> getCurrentTrack(@CurrentUser UserPrincipal userPrincipal, @PathVariable String partyCode) {
        partySecurityService.checkGetPartyInformationPermission(userPrincipal, partyCode);
        return null;
    }

}
