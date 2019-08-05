package sh.stern.cobralist.party.join.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sh.stern.cobralist.party.join.api.FindPartyDTO;
import sh.stern.cobralist.party.join.api.JoinPartyDTO;
import sh.stern.cobralist.party.join.api.JoinPartyService;
import sh.stern.cobralist.party.join.api.PartyJoinedDTO;

@RestController
@RequestMapping("/api/party")
public class JoinPartyController {

    private final JoinPartyService joinPartyService;

    @Autowired
    public JoinPartyController(JoinPartyService joinPartyService) {
        this.joinPartyService = joinPartyService;
    }

    @PostMapping("/join")
    public ResponseEntity<PartyJoinedDTO> joinParty(@RequestBody JoinPartyDTO joinPartyDto) {
        return ResponseEntity.ok(joinPartyService.joinParty(joinPartyDto));
    }

    @GetMapping("/find/{partyCode}")
    public ResponseEntity<FindPartyDTO> findParty(@PathVariable String partyCode) {
        return ResponseEntity.ok(joinPartyService.findParty(partyCode));
    }
}
