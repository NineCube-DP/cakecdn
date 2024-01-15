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

    @Bean
    @Primary
    AccountRepository mockAccountRepository(AccountRepository repository) {
        AccountRepository mock = mock(AccountRepository.class, AdditionalAnswers.delegatesTo(repository));

        AccountEntity admin = AccountEntity.builder()
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .id(0L)
                .role("ADMIN")
                .fullAccessPermission(false)
                .build();

        AccountEntity test = AccountEntity.builder()
                .username("test")
                .password(passwordEncoder.encode("password"))
                .id(1L)
                .fullAccessPermission(false)
                .build();

        when(mock.findByUsername("admin"))
                .thenReturn(Optional.of(admin));
        when(mock.findByUsername("test"))
                .thenReturn(Optional.of(test));

        return mock;
    }
}
