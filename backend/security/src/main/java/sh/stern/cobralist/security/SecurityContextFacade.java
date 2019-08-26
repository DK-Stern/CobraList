package sh.stern.cobralist.security;

import org.springframework.security.core.context.SecurityContext;

public interface SecurityContextFacade {
    SecurityContext getContext();

    void setContext(SecurityContext securityContext);
}
