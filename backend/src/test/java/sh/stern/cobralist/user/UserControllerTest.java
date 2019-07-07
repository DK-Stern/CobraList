package sh.stern.cobralist.user;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import sh.stern.cobralist.api.domains.SimplePlaylistDomain;
import sh.stern.cobralist.api.interfaces.UsersPlaylistsService;
import sh.stern.cobralist.helper.OAuth2Helper;
import sh.stern.cobralist.security.oauth2.user.UserPrincipal;
import sh.stern.cobralist.security.oauth2.user.model.AuthProvider;
import sh.stern.cobralist.security.oauth2.user.model.User;
import sh.stern.cobralist.security.oauth2.user.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OAuth2Helper oAuth2Helper;

    @MockBean
    private UserRepository userRepositoryMock;

    @MockBean
    private UsersPlaylistsService usersPlaylistsServiceMock;

    @Test
    public void getMyUser() throws Exception {
        // given
        final String name = "max";
        final String email = "max@meier.de";
        final long userId = 1L;

        final UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setId(userId);
        userPrincipal.setEmail(email);
        userPrincipal.setAuthProvider(AuthProvider.spotify);
        userPrincipal.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        String token = oAuth2Helper.createToken(userPrincipal);
        oAuth2Helper.loginUser(userPrincipal);

        final User expectedUser = new User();
        expectedUser.setId(userId);
        expectedUser.setName(name);
        expectedUser.setEmail(email);
        expectedUser.setProvider(AuthProvider.spotify);
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(expectedUser));

        // when & then
        mockMvc.perform(get("/api/user/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().json("{ id: " + userId
                        + ", name: '" + name
                        + "', email: '" + email
                        + "', authorities:[{authority:ROLE_USER}]}"));
    }

    @Test
    public void tryToLoadUserObjectWithoutLoggedInUser() throws Exception {
        // given
        final String email = "max@meier.de";
        final long userId = 1L;

        final UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setId(userId);
        userPrincipal.setEmail(email);
        userPrincipal.setAuthProvider(AuthProvider.spotify);
        userPrincipal.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        String token = oAuth2Helper.createToken(userPrincipal);
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());

        // when
        final MvcResult result = mockMvc.perform(get("/api/user/me")
                .header("Authorization", "Bearer " + token))
                .andReturn();

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result.getResponse().getErrorMessage()).isEqualTo("Full authentication is required " +
                "to access this resource");
        softly.assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        softly.assertAll();
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

        final User user = new User();
        user.setId(userId);
        user.setName("max");
        user.setEmail(email);
        user.setProvider(AuthProvider.spotify);
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(user));

        final String token = oAuth2Helper.createToken(userPrincipal);
        oAuth2Helper.loginUser(userPrincipal);

        final SimplePlaylistDomain expectedSimplePlaylistDomain = new SimplePlaylistDomain("1", "testPlaylist");
        when(usersPlaylistsServiceMock.getUsersPlaylists(userPrincipal.getUsername())).thenReturn(Collections.singletonList(expectedSimplePlaylistDomain));

        // when
        final MvcResult result = mockMvc.perform(get("/api/user/playlists")
                .header("Authorization", "Bearer " + token))
                .andReturn();

        // then
        assertThat(result.getResponse().getContentAsString()).isEqualTo("{\"playlists\":[{\"playlistId\":\"1\"," +
                "\"playlistName\":\"testPlaylist\"}]}");
    }
}