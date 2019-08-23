package sh.stern.cobralist.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import sh.stern.cobralist.party.persistence.dataaccess.GuestRepository;
import sh.stern.cobralist.party.persistence.dataaccess.UserRepository;
import sh.stern.cobralist.party.persistence.domain.User;
import sh.stern.cobralist.user.domain.StreamingProvider;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;
import sh.stern.cobralist.user.userprincipal.UserPrincipalBuilder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomUserDetailsServiceTest {

    private CustomUserDetailsService testSubject;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private UserPrincipalBuilder userPrincipleBuilderMock;

    @Mock
    private GuestRepository guestRepositoryMock;

    @Before
    public void setUp() {
        testSubject = new CustomUserDetailsService(userRepositoryMock,
                guestRepositoryMock,
                userPrincipleBuilderMock);
    }

    @Test
    public void loadUserByUsername() {
        // given
        final String email = "max@email.de";
        final User user = new User();
        final long userId = 1L;
        user.setId(userId);
        final String username = "max";
        user.setName(username);
        user.setEmail(email);
        user.setProvider(StreamingProvider.spotify);

        when(userRepositoryMock.findByEmail(email)).thenReturn(Optional.of(user));

        when(userPrincipleBuilderMock.withUserId(userId)).thenReturn(userPrincipleBuilderMock);
        when(userPrincipleBuilderMock.withName(username)).thenReturn(userPrincipleBuilderMock);
        when(userPrincipleBuilderMock.withEmail(email)).thenReturn(userPrincipleBuilderMock);
        when(userPrincipleBuilderMock.withAuthorities(anyList())).thenReturn(userPrincipleBuilderMock);
        when(userPrincipleBuilderMock.withProvider(StreamingProvider.spotify)).thenReturn(userPrincipleBuilderMock);
        final UserPrincipal expectedUserPrincipalMock = mock(UserPrincipal.class);
        when(userPrincipleBuilderMock.build()).thenReturn(expectedUserPrincipalMock);

        // when
        UserDetails resultedUserDetails = testSubject.loadUserByUsername(email);

        // then
        assertThat(resultedUserDetails).isEqualTo(expectedUserPrincipalMock);
    }

    @Test
    public void throwsUsernameNotFoundExceptionIfUserWithEmailDoesNotExist() {
        // given
        final String email = "max@mail.de";

        // then
        assertThatCode(() -> testSubject.loadUserByUsername(email))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found with mail: max@mail.de");
    }

    @Test
    public void loadUserById() {
        // given
        final long userId = 1L;
        final User user = new User();
        user.setId(userId);
        final String username = "max";
        user.setName(username);
        final String email = "max@mail.de";
        user.setEmail(email);
        user.setProvider(StreamingProvider.spotify);
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(user));

        when(userPrincipleBuilderMock.withUserId(userId)).thenReturn(userPrincipleBuilderMock);
        when(userPrincipleBuilderMock.withName(username)).thenReturn(userPrincipleBuilderMock);
        when(userPrincipleBuilderMock.withEmail(email)).thenReturn(userPrincipleBuilderMock);
        when(userPrincipleBuilderMock.withAuthorities(anyList())).thenReturn(userPrincipleBuilderMock);
        when(userPrincipleBuilderMock.withProvider(StreamingProvider.spotify)).thenReturn(userPrincipleBuilderMock);
        final UserPrincipal expectedUserPrincipalMock = mock(UserPrincipal.class);
        when(userPrincipleBuilderMock.build()).thenReturn(expectedUserPrincipalMock);

        // when
        final UserDetails resultedUserDetails = testSubject.loadUserById(userId);

        // then
        assertThat(resultedUserDetails).isEqualTo(expectedUserPrincipalMock);
    }

    @Test
    public void throwsResourceNotFoundExceptionIfUserWithIdNotFound() {
        // given
        final long userId = 1L;

        // then
        assertThatCode(() -> testSubject.loadUserById(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found with id : 1");
    }
}