package sh.stern.cobralist.vote.music.request.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh.stern.cobralist.security.CurrentUser;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;
import sh.stern.cobralist.vote.music.request.api.VoteMusicRequestDTO;
import sh.stern.cobralist.vote.music.request.api.VoteMusicRequestService;

@RestController
@RequestMapping("/api/musicrequest")
public class VoteMusicRequestController {

    private final VoteMusicRequestService voteMusicRequestService;

    @Autowired
    public VoteMusicRequestController(VoteMusicRequestService voteMusicRequestService) {
        this.voteMusicRequestService = voteMusicRequestService;
    }

    @PostMapping("/vote")
    @PreAuthorize("hasAnyRole('USER','GUEST')")
    public ResponseEntity voteMusicRequest(@CurrentUser UserPrincipal userPrincipal, @RequestBody VoteMusicRequestDTO voteMusicRequestDTO) {
        voteMusicRequestService.voteMusicRequest(userPrincipal, voteMusicRequestDTO);
        return ResponseEntity.ok().build();
    }

}
