package sh.stern.cobralist.user;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

public class UserResponse {
    private Long id;

    private String name;

    private String email;

    private Collection<SimpleGrantedAuthority> authorities;

    public UserResponse(Long id, String name, String email, Collection<SimpleGrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Collection<SimpleGrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<SimpleGrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
