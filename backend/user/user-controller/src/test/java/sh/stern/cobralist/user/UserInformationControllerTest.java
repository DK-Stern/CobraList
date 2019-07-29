package sh.stern.cobralist.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import sh.stern.cobralist.api.domains.SimplePlaylistDomain;
import sh.stern.cobralist.api.interfaces.UsersPlaylistsService;
import sh.stern.cobralist.persistence.dataaccess.UserRepository;
import sh.stern.cobralist.persistence.domain.User;
import sh.stern.cobralist.user.controller.BasePlaylistsResponse;
import sh.stern.cobralist.user.controller.UserInformationController;
import sh.stern.cobralist.user.controller.UserResponse;
import sh.stern.cobralist.user.domain.AuthProvider;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserInformationControllerTest {

    private UserInformationController testSubject;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private UsersPlaylistsService usersPlaylistsServiceMock;

    @Before
    public void setUp() {
        testSubject = new UserInformationController(userRepositoryMock, usersPlaylistsServiceMock);
    }

    @Test
    public void getMyUser() {
        // given
        final String name = "max";
        final String email = "max@meier.de";
        final long userId = 1L;
        final List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        final UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setId(userId);
        userPrincipal.setEmail(email);
        userPrincipal.setAuthProvider(AuthProvider.spotify);
        userPrincipal.setAuthorities(authorities);

        final User user = new User();
        user.setId(userId);
        user.setName(name);
        user.setEmail(email);
        user.setProvider(AuthProvider.spotify);
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(user));

        // when
        final ResponseEntity<UserResponse> resultedCurrentUser = testSubject.getCurrentUser(userPrincipal);

        // then
        final UserResponse expectedResponse = new UserResponse(userId, name, email, authorities);
        assertThat(resultedCurrentUser.getBody()).isEqualToComparingFieldByField(expectedResponse);
    }

    @Test
    public void getUsersPlaylists() throws Exception {
        // given
        final UserPrincipal userPrincipal = new UserPrincipal();
        final long userId = 1L;
        final String email = "test@mail.de";
        userPrincipal.setId(userId);
        userPrincipal.setEmail(email);
        userPrincipal.setAuthProvider(AuthProvider.spotify);
        userPrincipal.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        final List<SimplePlaylistDomain> expectedPlaylist = Collections.singletonList(new SimplePlaylistDomain("1", "testPlaylist"));
        when(usersPlaylistsServiceMock.getUsersPlaylists(userPrincipal.getUsername())).thenReturn(expectedPlaylist);

        // when
        final ResponseEntity<BasePlaylistsResponse> resultedUsersPlaylists = testSubject.getUsersPlaylists(userPrincipal);

        // then
        assertThat(Objects.requireNonNull(resultedUsersPlaylists.getBody()).getPlaylists()).isEqualTo(expectedPlaylist);
    }
}