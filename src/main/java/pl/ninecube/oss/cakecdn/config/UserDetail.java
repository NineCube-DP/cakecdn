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

import java.util.Collections;

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

    return new User(username, account.getPassword(), Collections.emptyList());
  }
}
