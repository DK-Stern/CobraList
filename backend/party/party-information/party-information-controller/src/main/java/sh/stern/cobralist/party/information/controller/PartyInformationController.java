package sh.stern.cobralist.party.information.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh.stern.cobralist.party.information.domain.PartyInformationDTO;
import sh.stern.cobralist.party.information.api.PartyInformationService;
import sh.stern.cobralist.security.CurrentUser;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

@RestController
@RequestMapping("/api/party")
public class PartyInformationController {

    private final PartyInformationService partyInformationService;

    @Autowired
    public PartyInformationController(PartyInformationService partyInformationService) {
        this.partyInformationService = partyInformationService;
    }

    @PreAuthorize("hasAnyRole('USER','GUEST')")
    @GetMapping("/{partyCode}")
    public ResponseEntity<PartyInformationDTO> getPartyInformation(@CurrentUser UserPrincipal userPrincipal, @PathVariable String partyCode) {
        return ResponseEntity.ok(partyInformationService.getPartyInformation(userPrincipal, partyCode));
    }
}
