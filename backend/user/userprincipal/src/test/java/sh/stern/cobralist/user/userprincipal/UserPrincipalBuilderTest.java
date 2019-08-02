package sh.stern.cobralist.user.userprincipal;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import sh.stern.cobralist.user.domain.AuthProvider;
import sh.stern.cobralist.user.domain.UserRole;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class UserPrincipalBuilderTest {

    private UserPrincipalBuilder testSubject;

    @Before
    public void setUp() {
        testSubject = new UserPrincipalBuilder();
    }

    @Test
    public void setIdOnBuildingUserPrinciple() {
        // given
        final Long id = 1L;
        testSubject.withPartyId(id)
                .withName("max")
                .withEmail("max@mail.de")
                .withProvider(AuthProvider.spotify)
                .withAuthorities(Collections.singletonList(new SimpleGrantedAuthority(UserRole.ROLE_USER.name())))
                .withAttributes(new HashMap<>());

        // when
        final UserPrincipal resultedUserPrincipal = testSubject.build();

        // then
        assertThat(resultedUserPrincipal.getId()).isEqualTo(id);
    }

    @Test
    public void throwsExceptionIfIdIsNotSet() {
        // given
        testSubject.withName("max")
                .withEmail("max@mail.de")
                .withProvider(AuthProvider.spotify);

        // then
        assertThatCode(() -> testSubject.build())
                .isInstanceOf(IllegalStateException.class)
                .withFailMessage("'id' is null!");
    }

    @Test
    public void setNameOnBuildingUserPrinciple() {
        // given
        final String name = "max";
        testSubject.withPartyId(1L)
                .withName(name)
                .withEmail("max@mail.de")
                .withProvider(AuthProvider.spotify)
                .withAuthorities(Collections.singletonList(new SimpleGrantedAuthority(UserRole.ROLE_USER.name())))
                .withAttributes(new HashMap<>());

        // when
        final UserPrincipal resultedUserPrincipal = testSubject.build();

        // then
        assertThat(resultedUserPrincipal.getName()).isEqualTo(name);
    }

    @Test
    public void throwsExceptionIfNameIsNotSet() {
        // given
        testSubject.withPartyId(1L)
                .withEmail("max@mail.de")
                .withProvider(AuthProvider.spotify);

        // then
        assertThatCode(() -> testSubject.build())
                .isInstanceOf(IllegalStateException.class)
                .withFailMessage("'name' is null!");
    }

    @Test
    public void setEmailOnBuildingUserPrinciple() {
        // given
        final String email = "max@mail.de";
        testSubject.withPartyId(1L)
                .withName("max")
                .withEmail(email)
                .withProvider(AuthProvider.spotify)
                .withAuthorities(Collections.singletonList(new SimpleGrantedAuthority(UserRole.ROLE_USER.name())))
                .withAttributes(new HashMap<>());

        // when
        final UserPrincipal resultedUserPrincipal = testSubject.build();

        // then
        assertThat(resultedUserPrincipal.getEmail()).isEqualTo(email);
    }

    @Test
    public void throwsExceptionIfEmailIsNotSet() {
        // given
        testSubject.withPartyId(1L)
                .withName("max")
                .withProvider(AuthProvider.spotify);

        // then
        assertThatCode(() -> testSubject.build())
                .isInstanceOf(IllegalStateException.class)
                .withFailMessage("'email' is null!");
    }

    @Test
    public void setAttributesOnBuildingUserPrinciple() {
        // given
        final HashMap<String, Object> attributes = new HashMap<>();
        testSubject.withPartyId(1L)
                .withName("max")
                .withEmail("max@mail.de")
                .withProvider(AuthProvider.spotify)
                .withAuthorities(Collections.singletonList(new SimpleGrantedAuthority(UserRole.ROLE_USER.name())))
                .withAttributes(attributes);

        // when
        final UserPrincipal resultedUserPrincipal = testSubject.build();

        // then
        assertThat(resultedUserPrincipal.getAttributes()).isEqualTo(attributes);
    }

    @Test
    public void setDefaultAuthoritiesIfAuthoritiesAreNotSet() {
        // given
        testSubject.withPartyId(1L)
                .withName("max")
                .withEmail("max@mail.de")
                .withProvider(AuthProvider.spotify)
                .withAuthorities(Collections.singletonList(new SimpleGrantedAuthority(UserRole.ROLE_USER.name())))
                .withAttributes(new HashMap<>());

        // when
        UserPrincipal resultedUserPrincipal = testSubject.build();

        // then
        assertThat(resultedUserPrincipal.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_USER");
    }

    @Test
    public void setAuthoritiesOnBuildingUserPrinciple() {
        // given
        final List<SimpleGrantedAuthority> expectedAuthorities = asList(new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN"));
        testSubject.withAuthorities(expectedAuthorities)
                .withPartyId(1L)
                .withName("max")
                .withEmail("max@mail.de")
                .withProvider(AuthProvider.spotify)
                .withAttributes(new HashMap<>());

        // when
        UserPrincipal resultedUserPrincipal = testSubject.build();

        // then
        assertThat(resultedUserPrincipal.getAuthorities()).isEqualTo(expectedAuthorities);
    }

    @Test
    public void setAuthProviderOnBuildingUserPrinciple() {
        // given
        final AuthProvider authProvider = AuthProvider.spotify;
        testSubject.withPartyId(1L)
                .withName("max")
                .withEmail("max@mail.de")
                .withProvider(authProvider)
                .withAuthorities(Collections.singletonList(new SimpleGrantedAuthority(UserRole.ROLE_USER.name())))
                .withAttributes(new HashMap<>());

        // when
        UserPrincipal resultedUserPrincipal = testSubject.build();

        // then
        assertThat(resultedUserPrincipal.getAuthProvider()).isEqualTo(authProvider);
    }

    @Test
    public void throwsExceptionIfAuthProviderIsNotSet() {
        // given
        final AuthProvider authProvider = AuthProvider.spotify;
        testSubject.withPartyId(1L)
                .withName("max")
                .withEmail("max@mail.de")
                .withAttributes(new HashMap<>());

        // then
        assertThatCode(() -> testSubject.build())
                .isInstanceOf(IllegalStateException.class)
                .withFailMessage("'authProvider' is null!");
    }

    @Test
    public void throwsExceptionIfAuthoritiesIsNotSet() {
        // given
        final AuthProvider authProvider = AuthProvider.spotify;
        testSubject.withPartyId(1L)
                .withName("max")
                .withEmail("max@mail.de");

        // then
        assertThatCode(() -> testSubject.build())
                .isInstanceOf(IllegalStateException.class)
                .withFailMessage("'authorities' is null!");
    }
}