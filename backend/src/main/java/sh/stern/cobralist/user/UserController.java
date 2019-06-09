package sh.stern.cobralist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sh.stern.cobralist.security.CurrentUser;
import sh.stern.cobralist.security.ResourceNotFoundException;
import sh.stern.cobralist.security.oauth2.user.UserPrincipal;
import sh.stern.cobralist.security.oauth2.user.model.User;
import sh.stern.cobralist.security.oauth2.user.repository.UserRepository;

@RestController
@RequestMapping("/api/user")
public class UserController {


    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/me")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
}
