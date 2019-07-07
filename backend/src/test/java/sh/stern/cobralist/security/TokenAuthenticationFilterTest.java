package sh.stern.cobralist.security;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import sh.stern.cobralist.security.oauth2.user.UserPrincipal;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TokenAuthenticationFilterTest {

    private TokenAuthenticationFilter testSubject;

    @Mock
    private TokenProvider tokenProviderMock;

    @Mock
    private CustomUserDetailsService customUserDetailsServiceMock;

    @Mock
    private OAuth2AuthorizedClientService oAuth2AuthorizedClientServiceMock;

    @Before
    public void setUp() {
        testSubject = new TokenAuthenticationFilter(tokenProviderMock,
                customUserDetailsServiceMock,
                oAuth2AuthorizedClientServiceMock);
    }

    @Test
    public void doFilterInternal() throws IOException, ServletException {
        // given
        final HttpServletRequest requestMock = mock(HttpServletRequest.class);
        final HttpServletResponse responseMock = mock(HttpServletResponse.class);
        final FilterChain filterChainMock = mock(FilterChain.class);

        final String jwt = "123";
        final String bearerJwt = "Bearer " + jwt;
        when(requestMock.getHeader("Authorization")).thenReturn(bearerJwt);

        when(tokenProviderMock.validateToken(jwt)).thenReturn(true);
        final long userId = 1L;
        when(tokenProviderMock.getUserIdFromToken(jwt)).thenReturn(userId);

        final UserPrincipal userPrincipalMock = mock(UserPrincipal.class);
        when(customUserDetailsServiceMock.loadUserById(userId)).thenReturn(userPrincipalMock);

        // when
        testSubject.doFilterInternal(requestMock, responseMock,
                filterChainMock);

        // then
        verify(filterChainMock).doFilter(requestMock, responseMock);
    }
}