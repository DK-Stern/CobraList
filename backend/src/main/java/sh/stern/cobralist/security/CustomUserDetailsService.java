package sh.stern.cobralist.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh.stern.cobralist.security.oauth2.user.UserPrincipal;
import sh.stern.cobralist.security.oauth2.user.UserPrincipalBuilder;
import sh.stern.cobralist.security.oauth2.user.model.User;
import sh.stern.cobralist.security.oauth2.user.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserPrincipalBuilder userPrincipalBuilder;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, UserPrincipalBuilder userPrincipalBuilder) {
        this.userRepository = userRepository;
        this.userPrincipalBuilder = userPrincipalBuilder;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) {
        final User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format("User not found with mail: %s", email)));

        return userPrincipalBuilder
                .withId(user.getId())
                .withName(user.getName())
                .withEmail(user.getEmail())
                .withProvider(user.getProvider())
                .build();
    }

    @Transactional
    public UserPrincipal loadUserById(Long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "id", id));

        return userPrincipalBuilder
                .withId(user.getId())
                .withName(user.getName())
                .withEmail(user.getEmail())
                .withProvider(user.getProvider())
                .build();
    }
}
