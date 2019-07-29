package sh.stern.cobralist.tokenprovider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.properties.AppProperties;
import sh.stern.cobralist.user.domain.UserRole;
import sh.stern.cobralist.user.userprincipal.UserPrincipal;

import java.util.Date;

@Service
public class TokenProvider {

    private static final Logger LOG = LoggerFactory.getLogger(TokenProvider.class);

    private AppProperties appProperties;

    @Autowired
    public TokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String createToken(UserPrincipal userPrincipal) {
        return createToken(userPrincipal, UserRole.ROLE_USER);
    }

    public String createToken(UserPrincipal userPrincipal, UserRole userRole) {
        final Date expirationDate = new Date(new Date().getTime() + appProperties.getAuth().getTokenExpirationMsec());

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .claim("role", userRole.name())
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
    }

    public String getRoleFromToken(String token) {
        final Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role").toString();
    }

    public Long getUserIdFromToken(String token) {
        final Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret())
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            LOG.error("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            LOG.error("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            LOG.error("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            LOG.error("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            LOG.error("JWT claims string is empty.");
        }
        return false;
    }
}
