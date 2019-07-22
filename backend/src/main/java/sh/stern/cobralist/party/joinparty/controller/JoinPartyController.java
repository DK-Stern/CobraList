package sh.stern.cobralist.party.joinparty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh.stern.cobralist.party.joinparty.api.JoinPartyDTO;
import sh.stern.cobralist.party.joinparty.api.JoinPartyService;
import sh.stern.cobralist.party.joinparty.api.PartyJoinedDTO;

@RestController
@RequestMapping("/api/party/join")
public class JoinPartyController {

    private final JoinPartyService joinPartyService;

    @Autowired
    public JoinPartyController(JoinPartyService joinPartyService) {
        this.joinPartyService = joinPartyService;
    }

    @PostMapping
    public ResponseEntity<PartyJoinedDTO> joinParty(@RequestBody JoinPartyDTO joinPartyDto) {
        return ResponseEntity.ok(joinPartyService.joinParty(joinPartyDto));
    }

}
