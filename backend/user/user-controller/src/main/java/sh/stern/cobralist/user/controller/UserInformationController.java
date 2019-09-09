package sh.stern.cobralist.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh.stern.cobralist.party.persistence.dataaccess.UserRepository;
import sh.stern.cobralist.party.persistence.domain.User;
import sh.stern.cobralist.security.CurrentUser;
import sh.stern.cobralist.security.ResourceNotFoundException;
import sh.stern.cobralist.streaming.api.PlaylistService;
import sh.stern.cobralist.streaming.domain.SimplePlaylistDTO;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserInformationController {


    private final UserRepository userRepository;
    private final PlaylistService playlistService;

    @Autowired
    public UserInformationController(UserRepository userRepository,
                                     PlaylistService playlistService) {
        this.userRepository = userRepository;
        this.playlistService = playlistService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/me")
    public ResponseEntity<UserResponse> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        final User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
        return ResponseEntity.ok(new UserResponse(user.getId(), user.getName(), user.getEmail(),
                userPrincipal.getAuthorities()));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/playlists")
    public ResponseEntity<BasePlaylistsResponse> getUsersPlaylists(@CurrentUser UserPrincipal userPrincipal) {
        List<SimplePlaylistDTO> usersPlaylists =
                playlistService.getUsersPlaylists(userPrincipal.getUsername());
        return ResponseEntity.ok(new BasePlaylistsResponse(usersPlaylists));
    }
}
