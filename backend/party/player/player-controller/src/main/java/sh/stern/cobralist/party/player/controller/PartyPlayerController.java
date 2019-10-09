package sh.stern.cobralist.party.player.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh.stern.cobralist.party.player.api.PartyPlayerService;
import sh.stern.cobralist.security.CurrentUser;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

@RestController
@RequestMapping("/api/party/player")
public class PartyPlayerController {

    private final PartyPlayerService partyPlayerService;

    @Autowired
    public PartyPlayerController(PartyPlayerService partyPlayerService) {
        this.partyPlayerService = partyPlayerService;
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/start/{partyCode}")
    public ResponseEntity startParty(@CurrentUser UserPrincipal userPrincipal, @PathVariable String partyCode) {
        partyPlayerService.startParty(userPrincipal, partyCode);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/stop/{partyCode}")
    public ResponseEntity stopParty(@CurrentUser UserPrincipal userPrincipal, @PathVariable String partyCode) {
        partyPlayerService.stopParty(userPrincipal, partyCode);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/skip/{partyCode}")
    public ResponseEntity skipMusicRequest(@CurrentUser UserPrincipal userPrincipal, @PathVariable String partyCode) {
        partyPlayerService.skipMusicRequest(userPrincipal, partyCode);
        return ResponseEntity.ok().build();
    }
}
