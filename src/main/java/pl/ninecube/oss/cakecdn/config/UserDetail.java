/* (C)2023 */
package pl.ninecube.oss.cakecdn.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.ninecube.oss.cakecdn.exception.BusinessException;
import pl.ninecube.oss.cakecdn.model.domain.Owner;
import pl.ninecube.oss.cakecdn.repository.AccountRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserDetail implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public Owner loadUserByUsername(String username) throws UsernameNotFoundException {
        var account =
                accountRepository
                        .findByUsername(username)
                        .orElseThrow(
                                () -> new BusinessException("User not exists by Username"));

        Owner.OwnerBuilder ownerBuilder = Owner.builder()
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .username(account.getUsername())
                .password(account.getPassword());


        if (Objects.nonNull(account.getRole()))
            ownerBuilder.role(account.getRole());
        else {
            ownerBuilder.role("USER");

            if (account.getFullAccessPermission())
                ownerBuilder.authority("full_access");
        }

        ownerBuilder.id(account.getId());

        return ownerBuilder.build();
    }
}
