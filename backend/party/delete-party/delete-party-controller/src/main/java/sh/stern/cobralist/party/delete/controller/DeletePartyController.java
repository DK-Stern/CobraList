package sh.stern.cobralist.party.delete.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh.stern.cobralist.party.delete.api.DeletePartyService;
import sh.stern.cobralist.security.CurrentUser;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

@RestController
@RequestMapping("/api/party")
public class DeletePartyController {

    private final DeletePartyService deletePartyService;

    @Autowired
    public DeletePartyController(DeletePartyService deletePartyService) {
        this.deletePartyService = deletePartyService;
    }

    @DeleteMapping("/delete/{partyCode}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity deleteParty(@CurrentUser UserPrincipal userPrincipal, @PathVariable String partyCode) {
        deletePartyService.deleteParty(userPrincipal, partyCode);
        return ResponseEntity.ok().build();
    }
}
