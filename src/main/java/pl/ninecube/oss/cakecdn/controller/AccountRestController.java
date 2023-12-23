package pl.ninecube.oss.cakecdn.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.ninecube.oss.cakecdn.model.dto.AccountCreateDto;
import pl.ninecube.oss.cakecdn.model.dto.AccountResponse;
import pl.ninecube.oss.cakecdn.model.dto.AccountUpdateDto;
import pl.ninecube.oss.cakecdn.service.AccountService;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Tag(name = "Managing accounts")
@SecurityRequirement(name = "basicAuth")
public class AccountRestController {

  private final AccountService accountService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Save new account")
  public AccountResponse createAccount(@Valid @RequestBody AccountCreateDto dto) {
    return accountService.saveAccount(dto);
  }

  @GetMapping("/{accountId}")
  @Operation(summary = "Get account by id")
  public AccountResponse readAccount(@PathVariable Long accountId) {
    return accountService.getAccountById(accountId);
  }

  @PutMapping("/{accountId}")
  @Operation(summary = "Update account data by id")
  public AccountResponse updateAccount(
          @PathVariable Long accountId, @RequestBody AccountUpdateDto dto) {
    return accountService.updateAccountById(accountId, dto);
  }

  @DeleteMapping("/{accountId}")
  @Operation(summary = "Delete account by id")
  public void deleteAccount(@PathVariable Long accountId) {
    accountService.deleteAccount(accountId);
  }
}
