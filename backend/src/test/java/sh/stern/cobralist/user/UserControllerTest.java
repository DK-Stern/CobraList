package sh.stern.cobralist.user;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import sh.stern.cobralist.security.TokenProvider;
import sh.stern.cobralist.security.oauth2.user.UserPrincipal;
import sh.stern.cobralist.security.oauth2.user.model.AuthProvider;
import sh.stern.cobralist.security.oauth2.user.model.User;
import sh.stern.cobralist.security.oauth2.user.repository.UserRepository;

import java.util.Optional;

import static org.mockito.Mockito.mock;
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
    private TokenProvider tokenProvider;

    @MockBean
    private UserRepository userRepositoryMock;

    @Test
    public void getMyUser() throws Exception {
        // given
        final Authentication authenticationMock = mock(Authentication.class);
        final UserPrincipal userPrincipal = new UserPrincipal();
        final long userId = 1L;
        userPrincipal.setId(userId);
        when(authenticationMock.getPrincipal()).thenReturn(userPrincipal);
        String token = tokenProvider.createToken(authenticationMock);

        final User expectedUser = new User();
        expectedUser.setId(userId);
        final String username = "max";
        expectedUser.setName(username);
        final String email = "max@meier.de";
        expectedUser.setEmail(email);
        expectedUser.setProvider(AuthProvider.spotify);
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(expectedUser));

        // when
        mockMvc.perform(get("/api/user/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().json("{ id: " + userId
                        + ", name: '" + username
                        + "', email: '" + email
                        + "', provider: 'spotify'}"));
    }

    @Test
    public void findMeNot() throws Exception {
        // given
        final Authentication authenticationMock = mock(Authentication.class);
        final UserPrincipal userPrincipal = new UserPrincipal();
        final long userId = 1L;
        userPrincipal.setId(userId);
        when(authenticationMock.getPrincipal()).thenReturn(userPrincipal);

        String token = tokenProvider.createToken(authenticationMock);
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());

        // when
        final MvcResult result = mockMvc.perform(get("/api/user/me")
                .header("Authorization", "Bearer " + token))
                .andReturn();

        // then
        final SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result.getResponse().getStatus()).isEqualTo(401);
        softly.assertThat(result.getResponse().getErrorMessage()).isEqualTo("Full authentication is required to " +
                "access this resource");
        softly.assertAll();
    }
}