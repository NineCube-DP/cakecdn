/* (C)2024 */
package pl.ninecube.oss.cakecdn.config;

import lombok.RequiredArgsConstructor;
import org.mockito.AdditionalAnswers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.ninecube.oss.cakecdn.model.entity.AccountEntity;
import pl.ninecube.oss.cakecdn.repository.AccountRepository;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor
@Profile({"integration"})
public class UserConfiguration {

    private final PasswordEncoder passwordEncoder;

    public static final String PLAIN_USER = "plain_user";
    public static final String ADMIN = "admin";

    @Bean
    @Primary
    AccountRepository mockAccountRepository(AccountRepository repository) {
        AccountRepository mock =
                mock(AccountRepository.class, AdditionalAnswers.delegatesTo(repository));

        AccountEntity admin =
                AccountEntity.builder()
                        .username(ADMIN)
                        .password(passwordEncoder.encode("password"))
                        .id(0L)
                        .role("ADMIN")
                        .fullAccessPermission(false)
                        .build();

        AccountEntity plainUser =
                AccountEntity.builder()
                        .username(PLAIN_USER)
                        .password(passwordEncoder.encode("password"))
                        .id(1L)
                        .fullAccessPermission(false)
                        .build();

        when(mock.findByUsername(ADMIN)).thenReturn(Optional.of(admin));
        when(mock.findByUsername(PLAIN_USER)).thenReturn(Optional.of(plainUser));

        return mock;
    }
}
