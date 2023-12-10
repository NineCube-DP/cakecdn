package pl.ninecube.oss.cakecdn.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import pl.ninecube.oss.cakecdn.BaseIntegrationTest;
import pl.ninecube.oss.cakecdn.model.dto.OwnerCreateDto;
import pl.ninecube.oss.cakecdn.model.dto.OwnerResponse;
import pl.ninecube.oss.cakecdn.model.entity.OwnerEntity;
import pl.ninecube.oss.cakecdn.repository.OwnerRepository;

import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class OwnerRestControllerIT extends BaseIntegrationTest {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private ObjectMapper objectMapper;

//    @Autowired
//    PasswordEncoder passwordEncoder;

    private static final Faker faker = new Faker();

    @Test
    public void shouldSaveNewOwnerTest() throws Exception {
        var ownerCreateDto = OwnerCreateDto.builder()
                .username(faker.name().username())
//                .password(passwordEncoder.encode("test"))
                .password(faker.password().toString())
                .build();

        var result = mvc.perform(post("/owner")
                        .content(objectMapper.writeValueAsString(ownerCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        var content = result.getResponse().getContentAsString();

        var response = objectMapper.readValue(content, OwnerResponse.class);

        assert ownerRepository.existsById(response.getId());
    }

    @Test
    public void shouldReturnExistingOwnerTest() throws Exception {
        var saved = ownerRepository.save(OwnerEntity.builder()
                .username(faker.name().username())
                .password(faker.password().toString())
                .build());

        var result = mvc.perform(get("/owner/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andReturn();

        var content = result.getResponse().getContentAsString();

        var response = objectMapper.readValue(content, OwnerResponse.class);

        assert response.getUsername().equalsIgnoreCase(saved.getUsername());
        assert Objects.equals(response.getId(), saved.getId());
    }

    @Test
    public void shouldReturnErrorOnNonExistingOwnerTest() throws Exception {
        mvc.perform(get("/owner/{id}", faker.number().randomDigitNotZero()))
                .andExpect(status().isNotFound());

    }
}