package sh.stern.cobralist.party.information.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh.stern.cobralist.party.current.track.api.CurrentTrackService;
import sh.stern.cobralist.party.security.api.PartySecurityService;
import sh.stern.cobralist.security.CurrentUser;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

@RestController
@RequestMapping("/api/party")
public class PartyInformationController {

    private final PartySecurityService partySecurityService;
    private final CurrentTrackService currentTrackService;

    @Autowired
    public PartyInformationController(
            PartySecurityService partySecurityService,
            CurrentTrackService currentTrackService
    ) {
        this.partySecurityService = partySecurityService;
        this.currentTrackService = currentTrackService;
    }

    @PreAuthorize("hasAnyRole('USER','GUEST')")
    @GetMapping("/{partyCode}")
    public ResponseEntity<PartyInformationDTO> getPartyInformation(@CurrentUser UserPrincipal userPrincipal, @PathVariable String partyCode) {
        partySecurityService.checkGetPartyInformationPermission(userPrincipal, partyCode);

        final PartyInformationDTO partyInformationDTO = new PartyInformationDTO();
        partyInformationDTO.setCurrentPlaybackDTO(currentTrackService.getCurrentTrack(partyCode));

        return ResponseEntity.ok(partyInformationDTO);
    }

}
