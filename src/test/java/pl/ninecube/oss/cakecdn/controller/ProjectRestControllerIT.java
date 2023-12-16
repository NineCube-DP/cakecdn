package pl.ninecube.oss.cakecdn.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import pl.ninecube.oss.cakecdn.BaseIntegrationTest;
import pl.ninecube.oss.cakecdn.model.dto.AccountResponse;
import pl.ninecube.oss.cakecdn.model.dto.ProjectCreateDto;
import pl.ninecube.oss.cakecdn.model.dto.ProjectResponse;
import pl.ninecube.oss.cakecdn.model.dto.ProjectUpdateDto;
import pl.ninecube.oss.cakecdn.model.entity.AccountEntity;
import pl.ninecube.oss.cakecdn.model.entity.ProjectEntity;
import pl.ninecube.oss.cakecdn.repository.AccountRepository;
import pl.ninecube.oss.cakecdn.repository.ProjectRepository;

import java.util.Objects;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ProjectRestControllerIT extends BaseIntegrationTest {

    private final AccountRepository accountRepository;
    private final ProjectRepository projectRepository;
    private final ObjectMapper objectMapper;

    @Test
    public void shouldSaveNewAccountTest() throws Exception {
        var projectCreateDto = ProjectCreateDto.builder()
                .name(faker.pokemon().name())
                .baseUrl(faker.internet().url())
                .build();

        var result = mvc.perform(post("/project")
                        .with(httpBasic("admin", "password"))
                        .content(objectMapper.writeValueAsString(projectCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        var content = result.getResponse().getContentAsString();

        var response = objectMapper.readValue(content, AccountResponse.class);

        assert projectRepository.existsById(response.getId());
    }

    @Test
    public void shouldReturnUnauthorizedTest() throws Exception {
        var projectCreateDto = ProjectCreateDto.builder()
                .name(faker.pokemon().name())
                .baseUrl(faker.internet().url())
                .build();

        mvc.perform(post("/project")
                        .with(httpBasic("non_existing_user", "password"))
                        .content(objectMapper.writeValueAsString(projectCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldReturnExistingProjectTest() throws Exception {
        AccountEntity account = accountRepository.findByUsername("admin")
                .orElse(null);
        assert Objects.nonNull(account);

        var saved = projectRepository.save(ProjectEntity.builder()
                .owner(account)
                .name(faker.pokemon().name())
                .baseUrl(faker.internet().url())
                .enabled(false)
                .build());

        var result = mvc.perform(get("/project/{id}", saved.getId())
                        .with(httpBasic("admin", "password")))
                .andExpect(status().isOk())
                .andReturn();

        var content = result.getResponse().getContentAsString();

        var response = objectMapper.readValue(content, ProjectResponse.class);

        assert response.getName().equalsIgnoreCase(saved.getName());
        assert response.getBaseUrl().equalsIgnoreCase(saved.getBaseUrl());
        assert response.isEnabled() == saved.isEnabled();
        assert Objects.equals(response.getId(), saved.getId());
    }

    @Test
    public void shouldReturnErrorOnNonExistingProjectTest() throws Exception {
        mvc.perform(get("/project/{id}", faker.number().randomDigitNotZero())
                        .with(httpBasic("admin", "password")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldUpdateProjectTest() throws Exception {
        var projectCreateDto = ProjectCreateDto.builder()
                .name(faker.pokemon().name())
                .baseUrl(faker.internet().url())
                .build();


        var resultCreate = mvc.perform(post("/project")
                        .with(httpBasic("admin", "password"))
                        .content(objectMapper.writeValueAsString(projectCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        var content = resultCreate.getResponse().getContentAsString();

        var response = objectMapper.readValue(content, ProjectResponse.class);

        var project = projectRepository.findById(response.getId()).orElse(null);

        assert Objects.nonNull(project);

        var projectUpdateDto = ProjectUpdateDto.builder()
                .baseUrl(faker.internet().url())
                .enabled(true)
                .build();

        var resultUpdate = mvc.perform(put("/project/{id}", project.getId())
                        .with(httpBasic("admin", "password"))
                        .content(objectMapper.writeValueAsString(projectUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        content = resultUpdate.getResponse().getContentAsString();

        response = objectMapper.readValue(content, ProjectResponse.class);

        var updatedAccount = projectRepository.findById(response.getId()).orElse(null);


        assert Objects.nonNull(updatedAccount);
        assert response.getName().equalsIgnoreCase(project.getName());
        assert response.getBaseUrl().equalsIgnoreCase(projectUpdateDto.getBaseUrl());
        assert response.isEnabled();
    }


}