package sh.stern.cobralist.security.oauth2.user;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

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
        testSubject.withId(id);
        testSubject.withName("max");
        testSubject.withEmail("max@mail.de");
        testSubject.withAttributes(new HashMap<>());

        // when
        final UserPrincipal resultedUserPrincipal = testSubject.build();

        // then
        assertThat(resultedUserPrincipal.getId()).isEqualTo(id);
    }

    @Test
    public void throwsExceptionIfIdIsNotSet() {
        // given
        testSubject.withName("max");
        testSubject.withEmail("max@mail.de");

        // then
        assertThatCode(() -> testSubject.build())
                .isInstanceOf(IllegalStateException.class)
                .withFailMessage("'id' is null!");
    }

    @Test
    public void setNameOnBuildingUserPrinciple() {
        // given
        testSubject.withId(1L);
        final String name = "max";
        testSubject.withName(name);
        testSubject.withEmail("max@mail.de");
        testSubject.withAttributes(new HashMap<>());

        // when
        final UserPrincipal resultedUserPrincipal = testSubject.build();

        // then
        assertThat(resultedUserPrincipal.getName()).isEqualTo(name);
    }

    @Test
    public void throwsExceptionIfNameIsNotSet() {
        // given
        testSubject.withId(1L);
        testSubject.withEmail("max@mail.de");

        // then
        assertThatCode(() -> testSubject.build())
                .isInstanceOf(IllegalStateException.class)
                .withFailMessage("'name' is null!");
    }

    @Test
    public void setEmailOnBuildingUserPrinciple() {
        // given
        testSubject.withId(1L);
        testSubject.withName("max");
        final String email = "max@mail.de";
        testSubject.withEmail(email);
        testSubject.withAttributes(new HashMap<>());

        // when
        final UserPrincipal resultedUserPrincipal = testSubject.build();

        // then
        assertThat(resultedUserPrincipal.getEmail()).isEqualTo(email);
    }

    @Test
    public void throwsExceptionIfEmailIsNotSet() {
        // given
        testSubject.withId(1L);
        testSubject.withName("max");

        // then
        assertThatCode(() -> testSubject.build())
                .isInstanceOf(IllegalStateException.class)
                .withFailMessage("'email' is null!");
    }

    @Test
    public void setAttributesOnBuildingUserPrinciple() {
        // given
        testSubject.withId(1L);
        testSubject.withName("max");
        testSubject.withEmail("max@mail.de");
        final HashMap<String, Object> attributes = new HashMap<>();
        testSubject.withAttributes(attributes);

        // when
        final UserPrincipal resultedUserPrincipal = testSubject.build();

        // then
        assertThat(resultedUserPrincipal.getAttributes()).isEqualTo(attributes);
    }

    @Test
    public void setDefaultAuthoritiesIfAuthoritiesAreNotSet() {
        // given
        testSubject.withId(1L);
        testSubject.withName("max");
        testSubject.withEmail("max@mail.de");
        testSubject.withAttributes(new HashMap<>());

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
        testSubject.withAuthorities(expectedAuthorities);
        testSubject.withId(1L);
        testSubject.withName("max");
        testSubject.withEmail("max@mail.de");
        testSubject.withAttributes(new HashMap<>());

        // when
        UserPrincipal resultedUserPrincipal = testSubject.build();

        // then
        assertThat(resultedUserPrincipal.getAuthorities()).isEqualTo(expectedAuthorities);
    }
}