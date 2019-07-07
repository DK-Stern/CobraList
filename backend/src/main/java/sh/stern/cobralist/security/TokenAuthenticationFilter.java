package sh.stern.cobralist.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import sh.stern.cobralist.security.oauth2.OAuth2SessionExpiredException;
import sh.stern.cobralist.security.oauth2.user.UserPrincipal;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    private final TokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    public TokenAuthenticationFilter(TokenProvider tokenProvider,
                                     CustomUserDetailsService customUserDetailsService,
                                     OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
        this.tokenProvider = tokenProvider;
        this.customUserDetailsService = customUserDetailsService;
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {

            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Long userId = tokenProvider.getUserIdFromToken(jwt);

                UserPrincipal userDetails = customUserDetailsService.loadUserById(userId);

                // TODO Refresh token
                if (oAuth2AuthorizedClientService.loadAuthorizedClient(userDetails.getAuthProvider().name(),
                        userDetails.getUsername()) == null) {
                    throw new OAuth2SessionExpiredException("OAuth2 session abgelaufen.");
                }

                final UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {
            LOG.error("Could not set user authentication in security context.", e);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
