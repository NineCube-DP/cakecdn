package pl.ninecube.oss.cakecdn.model.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.function.Function;

@Getter
@Builder
public class Owner implements UserDetails, CredentialsContainer {
    Long id;

    private String username;
    private String password;

    private List<GrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    @Builder.Default
    private Function<String, String> passwordEncoder = (password) -> password;

    private Map<String, Object> details;

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    public static class OwnerBuilder {
        private List<GrantedAuthority> authorities = new LinkedList<>();
        private Map<String, Object> details = new HashMap<>();

        public OwnerBuilder role(String role) {
            this.authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            return this;
        }

        public OwnerBuilder authority(String authority) {
            this.authorities.add(new SimpleGrantedAuthority(authority));
            return this;
        }

        public OwnerBuilder detail(String name, Object object) {
            this.details.put(name, object);
            return this;
        }

//        public OwnerBuilder passwordEncoder(Function<String, String> encoder) {
//            Assert.notNull(encoder, "encoder cannot be null");
//            this.passwordEncoder = encoder;
//            return this;
//        }
    }
}
