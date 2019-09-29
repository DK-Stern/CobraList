package sh.stern.cobralist.music.request.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh.stern.cobralist.add.music.request.api.AddMusicRequestDTO;
import sh.stern.cobralist.add.music.request.api.AddMusicRequestService;
import sh.stern.cobralist.security.CurrentUser;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

@RestController
@RequestMapping("/api/musicrequest")
public class AddMusicRequestController {

    private final AddMusicRequestService addMusicRequestService;

    @Autowired
    public AddMusicRequestController(AddMusicRequestService addMusicRequestService) {
        this.addMusicRequestService = addMusicRequestService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('USER','GUEST')")
    public ResponseEntity addMusicRequest(@CurrentUser UserPrincipal userPrincipal, @RequestBody AddMusicRequestDTO addMusicRequestDTO) {
        addMusicRequestService.addMusicRequest(userPrincipal, addMusicRequestDTO);
        return ResponseEntity.ok().build();
    }
}
