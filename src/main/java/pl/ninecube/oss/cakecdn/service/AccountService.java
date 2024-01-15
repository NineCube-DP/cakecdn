/* (C)2023 */
package pl.ninecube.oss.cakecdn.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ninecube.oss.cakecdn.exception.ResourceNotExistException;
import pl.ninecube.oss.cakecdn.model.dto.AccountCreateDto;
import pl.ninecube.oss.cakecdn.model.dto.AccountResponse;
import pl.ninecube.oss.cakecdn.model.dto.AccountUpdateDto;
import pl.ninecube.oss.cakecdn.model.mapper.AccountMapper;
import pl.ninecube.oss.cakecdn.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;
  private final AccountMapper accountMapper;

  public AccountResponse saveAccount(AccountCreateDto dto) {
    var account = accountMapper.toDomain(dto);
    var entity = accountRepository.save(accountMapper.toEntity(account));

    return accountMapper.toResponse(entity);
  }

  public AccountResponse getAccountById(Long accountId) {
    var entity =
            accountRepository
                    .findById(accountId)
                    .orElseThrow(() -> new ResourceNotExistException("Account not found"));
    return accountMapper.toResponse(entity);
  }

  public AccountResponse updateAccountById(Long accountId, AccountUpdateDto dto) {
    var entity =
            accountRepository
                    .findById(accountId)
                    .orElseThrow(() -> new ResourceNotExistException("Account not found"));

    var updated = accountMapper.update(accountMapper.toDomain(entity), dto);

    var updatedEntity = accountRepository.save(accountMapper.toEntity(updated));

    return accountMapper.toResponse(updatedEntity);
  }

  public void deleteAccount(Long accountId) {
    accountRepository.deleteById(accountId);
  }
}
