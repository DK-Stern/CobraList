package sh.stern.cobralist.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh.stern.cobralist.persistence.dataaccess.GuestRepository;
import sh.stern.cobralist.persistence.dataaccess.UserRepository;
import sh.stern.cobralist.persistence.domain.Guest;
import sh.stern.cobralist.persistence.domain.User;
import sh.stern.cobralist.user.domain.UserRole;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;
import sh.stern.cobralist.user.userprincipal.UserPrincipalBuilder;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final GuestRepository guestRepository;
    private final UserPrincipalBuilder userPrincipalBuilder;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository,
                                    GuestRepository guestRepository,
                                    UserPrincipalBuilder userPrincipalBuilder) {
        this.userRepository = userRepository;
        this.guestRepository = guestRepository;
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
                .withAuthorities(Collections.singletonList(new SimpleGrantedAuthority(UserRole.ROLE_USER.name())))
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
                .withAuthorities(Collections.singletonList(new SimpleGrantedAuthority(UserRole.ROLE_USER.name())))
                .build();
    }

    @Transactional
    public UserPrincipal loadGuestById(Long id) {
        final Guest guest = guestRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Guest", "id", id));

        return userPrincipalBuilder
                .withId(guest.getId())
                .withName(guest.getName())
                .withAuthorities(Collections.singletonList(new SimpleGrantedAuthority(UserRole.ROLE_GUEST.name())))
                .build();
    }
}
