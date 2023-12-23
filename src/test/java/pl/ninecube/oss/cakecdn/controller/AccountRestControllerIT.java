/* (C)2023 */
package pl.ninecube.oss.cakecdn.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import pl.ninecube.oss.cakecdn.BaseIntegrationTest;
import pl.ninecube.oss.cakecdn.model.dto.AccountCreateDto;
import pl.ninecube.oss.cakecdn.model.dto.AccountResponse;
import pl.ninecube.oss.cakecdn.model.dto.AccountUpdateDto;
import pl.ninecube.oss.cakecdn.model.entity.AccountEntity;
import pl.ninecube.oss.cakecdn.repository.AccountRepository;

import java.util.Objects;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AccountRestControllerIT extends BaseIntegrationTest {

  private final AccountRepository accountRepository;
  private final ObjectMapper objectMapper;

  @Test
  public void shouldSaveNewAccountTest() throws Exception {
    var accountCreateDto =
            AccountCreateDto.builder()
                    .username(faker.name().username())
                    .password(faker.internet().password())
                    .build();

    var result =
            mvc.perform(
                            post("/account")
                                    .with(httpBasic("admin", "password"))
                                    .content(objectMapper.writeValueAsString(accountCreateDto))
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andReturn();

    var content = result.getResponse().getContentAsString();

    var response = objectMapper.readValue(content, AccountResponse.class);

    assert accountRepository.existsById(response.getId());
  }

  @Test
  public void shouldEncryptPasswordTest() throws Exception {
    var accountCreateDto =
            AccountCreateDto.builder()
                    .username(faker.name().username())
                    .password(faker.internet().password())
                    .build();

    var result =
            mvc.perform(
                            post("/account")
                                    .with(httpBasic("admin", "password"))
                                    .content(objectMapper.writeValueAsString(accountCreateDto))
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andReturn();

    var content = result.getResponse().getContentAsString();

    var response = objectMapper.readValue(content, AccountResponse.class);

    var account = accountRepository.findById(response.getId()).orElse(null);

    assert Objects.nonNull(account);
    assert !account.getPassword().equals(accountCreateDto.getPassword());
  }

  @Test
  public void shouldReturnUnauthorizedTest() throws Exception {
    var accountCreateDto =
            AccountCreateDto.builder()
                    .username(faker.name().username())
                    .password(faker.internet().password())
                    .build();

    mvc.perform(
                    post("/account")
                            .with(httpBasic("non_existing_user", "password"))
                            .content(objectMapper.writeValueAsString(accountCreateDto))
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
  }

  @Test
  public void shouldReturnExistingAccountTest() throws Exception {
    var saved =
            accountRepository.save(
                    AccountEntity.builder()
                            .username(faker.name().username())
                            .password(faker.internet().password())
                            .build());

    var result =
            mvc.perform(
                            get("/account/{id}", saved.getId())
                                    .with(httpBasic("admin", "password")))
                    .andExpect(status().isOk())
                    .andReturn();

    var content = result.getResponse().getContentAsString();

    var response = objectMapper.readValue(content, AccountResponse.class);

    assert response.getUsername().equalsIgnoreCase(saved.getUsername());
    assert Objects.equals(response.getId(), saved.getId());
  }

  @Test
  public void shouldReturnErrorOnNonExistingAccountTest() throws Exception {
    mvc.perform(
                    get("/account/{id}", faker.number().randomDigitNotZero())
                            .with(httpBasic("admin", "password")))
            .andExpect(status().isNotFound());
  }

  @Test
  public void shouldUpdateAccountTest() throws Exception {
    var accountCreateDto =
            AccountCreateDto.builder()
                    .username(faker.name().username())
                    .password(faker.internet().password())
                    .build();

    var resultCreate =
            mvc.perform(
                            post("/account")
                                    .with(httpBasic("admin", "password"))
                                    .content(objectMapper.writeValueAsString(accountCreateDto))
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andReturn();

    var content = resultCreate.getResponse().getContentAsString();

    var response = objectMapper.readValue(content, AccountResponse.class);

    var savedAccount = accountRepository.findById(response.getId()).orElse(null);

    assert Objects.nonNull(savedAccount);

    var accountUpdateDto =
            AccountUpdateDto.builder()
                    .username(faker.name().username())
                    .password(faker.internet().password())
                    .build();

    var resultUpdate =
            mvc.perform(
                            put("/account/{id}", savedAccount.getId())
                                    .with(httpBasic("admin", "password"))
                                    .content(objectMapper.writeValueAsString(accountUpdateDto))
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

    content = resultUpdate.getResponse().getContentAsString();

    response = objectMapper.readValue(content, AccountResponse.class);

    var updatedAccount = accountRepository.findById(response.getId()).orElse(null);

    assert Objects.nonNull(updatedAccount);
    assert !updatedAccount.getPassword().equals(savedAccount.getPassword());
    assert !updatedAccount.getUsername().equals(savedAccount.getUsername());
  }
}
