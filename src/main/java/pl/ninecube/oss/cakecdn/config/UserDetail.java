/* (C)2023 */
package pl.ninecube.oss.cakecdn.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.ninecube.oss.cakecdn.repository.AccountRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Profile("!integration")
public class UserDetail implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var account =
                accountRepository
                        .findByUsername(username)
                        .orElseThrow(
                                () -> new UsernameNotFoundException("User not exists by Username"));

        User.UserBuilder userBuilder = User.builder()
                .username(account.getUsername())
                .password(account.getPassword());


        if (Objects.nonNull(account.getRole()))
            userBuilder.roles(account.getRole());
        else {
            userBuilder.roles("USER");

            if (account.getFullAccessPermission())
                userBuilder.authorities("full_access");
        }

        return userBuilder.build();
    }
}
