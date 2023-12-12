package pl.ninecube.oss.cakecdn.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import pl.ninecube.oss.cakecdn.BaseIntegrationTest;
import pl.ninecube.oss.cakecdn.model.dto.OwnerCreateDto;
import pl.ninecube.oss.cakecdn.model.dto.OwnerResponse;
import pl.ninecube.oss.cakecdn.model.dto.OwnerUpdateDto;
import pl.ninecube.oss.cakecdn.model.entity.OwnerEntity;
import pl.ninecube.oss.cakecdn.repository.OwnerRepository;

import java.util.Objects;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class OwnerRestControllerIT extends BaseIntegrationTest {

    private final OwnerRepository ownerRepository;
    private final ObjectMapper objectMapper;

    @Test
    public void shouldSaveNewOwnerTest() throws Exception {
        var ownerCreateDto = OwnerCreateDto.builder()
                .username(faker.name().username())
                .password(faker.internet().password())
                .build();

        var result = mvc.perform(post("/owner")
                        .with(httpBasic("admin", "password"))
                        .content(objectMapper.writeValueAsString(ownerCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        var content = result.getResponse().getContentAsString();

        var response = objectMapper.readValue(content, OwnerResponse.class);

        assert ownerRepository.existsById(response.getId());
    }

    @Test
    public void shouldEncryptPasswordTest() throws Exception {
        var ownerCreateDto = OwnerCreateDto.builder()
                .username(faker.name().username())
                .password(faker.internet().password())
                .build();

        var result = mvc.perform(post("/owner")
                        .with(httpBasic("admin", "password"))
                        .content(objectMapper.writeValueAsString(ownerCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        var content = result.getResponse().getContentAsString();

        var response = objectMapper.readValue(content, OwnerResponse.class);

        var owner = ownerRepository.findById(response.getId()).orElse(null);

        assert Objects.nonNull(owner);
        assert !owner.getPassword().equals(ownerCreateDto.getPassword());
    }

    @Test
    public void shouldReturnUnauthorizedTest() throws Exception {
        var ownerCreateDto = OwnerCreateDto.builder()
                .username(faker.name().username())
                .password(faker.internet().password())
                .build();

        mvc.perform(post("/owner")
                        .with(httpBasic("non_existing_user", "password"))
                        .content(objectMapper.writeValueAsString(ownerCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldReturnExistingOwnerTest() throws Exception {
        var saved = ownerRepository.save(OwnerEntity.builder()
                .username(faker.name().username())
                .password(faker.internet().password())
                .build());

        var result = mvc.perform(get("/owner/{id}", saved.getId())
                        .with(httpBasic("admin", "password")))
                .andExpect(status().isOk())
                .andReturn();

        var content = result.getResponse().getContentAsString();

        var response = objectMapper.readValue(content, OwnerResponse.class);

        assert response.getUsername().equalsIgnoreCase(saved.getUsername());
        assert Objects.equals(response.getId(), saved.getId());
    }

    @Test
    public void shouldReturnErrorOnNonExistingOwnerTest() throws Exception {
        mvc.perform(get("/owner/{id}", faker.number().randomDigitNotZero())
                        .with(httpBasic("admin", "password")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldUpdateOwnerTest() throws Exception {
        var ownerCreateDto = OwnerCreateDto.builder()
                .username(faker.name().username())
                .password(faker.internet().password())
                .build();

        var resultCreate = mvc.perform(post("/owner")
                        .with(httpBasic("admin", "password"))
                        .content(objectMapper.writeValueAsString(ownerCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        var content = resultCreate.getResponse().getContentAsString();

        var response = objectMapper.readValue(content, OwnerResponse.class);

        var savedOwner = ownerRepository.findById(response.getId()).orElse(null);

        assert Objects.nonNull(savedOwner);

        var ownerUpdateDto = OwnerUpdateDto.builder()
                .username(faker.name().username())
                .password(faker.internet().password())
                .build();

        var resultUpdate = mvc.perform(put("/owner/{id}", savedOwner.getId())
                        .with(httpBasic("admin", "password"))
                        .content(objectMapper.writeValueAsString(ownerUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        content = resultUpdate.getResponse().getContentAsString();

        response = objectMapper.readValue(content, OwnerResponse.class);

        var updatedOwner = ownerRepository.findById(response.getId()).orElse(null);


        assert Objects.nonNull(updatedOwner);
        assert !updatedOwner.getPassword().equals(savedOwner.getPassword());
        assert !updatedOwner.getUsername().equals(savedOwner.getUsername());
    }


}