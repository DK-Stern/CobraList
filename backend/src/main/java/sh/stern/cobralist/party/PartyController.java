package sh.stern.cobralist.party;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh.stern.cobralist.party.persistence.Party;
import sh.stern.cobralist.security.CurrentUser;
import sh.stern.cobralist.security.oauth2.user.UserPrincipal;

@RestController
@RequestMapping("/api/party")
public class PartyController {

    private final PartyService partyService;

    @Autowired
    public PartyController(PartyService partyService) {
        this.partyService = partyService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public ResponseEntity createParty(@CurrentUser UserPrincipal userPrincipal,
                                      @RequestBody PartyCreationDTO partyRequest) {
        Party party = partyService.createParty(partyRequest, userPrincipal.getId());

        PartyCreationResponse partyCreationResponse = new PartyCreationResponse();
        partyCreationResponse.setId(party.getId());
        partyCreationResponse.setName(party.getName());
        partyCreationResponse.setPassword(party.getPassword());

        return ResponseEntity.ok(partyCreationResponse);
    }

}
