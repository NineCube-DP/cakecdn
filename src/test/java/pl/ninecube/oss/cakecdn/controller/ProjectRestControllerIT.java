/* (C)2023-2024 */
package pl.ninecube.oss.cakecdn.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import pl.ninecube.oss.cakecdn.BaseIntegrationTest;
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
import static pl.ninecube.oss.cakecdn.config.UserConfiguration.PLAIN_USER;

@WithUserDetails(PLAIN_USER)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ProjectRestControllerIT extends BaseIntegrationTest {

    private final AccountRepository accountRepository;
    private final ProjectRepository projectRepository;

    @Test
    public void shouldSaveNewAccountTest() throws Exception {
        var projectCreateDto =
                ProjectCreateDto.builder()
                        .name(faker.pokemon().name())
                        .baseUrl("https://" + faker.internet().url())
                        .build();

        var result =
                mvc.perform(
                                post("/project")
                                        .content(objectMapper.writeValueAsString(projectCreateDto))
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andReturn();

        var content = result.getResponse().getContentAsString();

        var response = objectMapper.readValue(content, ProjectResponse.class);

        assert projectRepository.existsById(response.getId());
    }

    @Test
    public void shouldReturnUnauthorizedTest() throws Exception {
        var projectCreateDto =
                ProjectCreateDto.builder()
                        .name(faker.pokemon().name())
                        .baseUrl("https://" + faker.internet().url())
                        .build();

        mvc.perform(
                        post("/project")
                                .with(httpBasic("non_existing_user", "password"))
                                .content(objectMapper.writeValueAsString(projectCreateDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldReturnExistingProjectTest() throws Exception {
        AccountEntity account = accountRepository.findByUsername(PLAIN_USER).orElse(null);
        assert Objects.nonNull(account);

        var saved =
                projectRepository.save(
                        ProjectEntity.builder()
                                .ownerId(account.getId())
                                .name(faker.pokemon().name())
                                .baseUrl("https://" + faker.internet().url())
                                .enabled(false)
                                .build());

        var result =
                mvc.perform(get("/project/{id}", saved.getId()))
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
        mvc.perform(get("/project/{id}", faker.number().numberBetween(9999, 99999)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldUpdateProjectTest() throws Exception {
        var projectCreateDto =
                ProjectCreateDto.builder()
                        .name(faker.pokemon().name())
                        .baseUrl("https://" + faker.internet().url())
                        .build();

        var resultCreate =
                mvc.perform(
                                post("/project")
                                        .content(objectMapper.writeValueAsString(projectCreateDto))
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andReturn();

        var content = resultCreate.getResponse().getContentAsString();

        var response = objectMapper.readValue(content, ProjectResponse.class);

        var project = projectRepository.findById(response.getId()).orElse(null);

        assert Objects.nonNull(project);

        var projectUpdateDto =
                ProjectUpdateDto.builder()
                        .baseUrl("https://" + faker.internet().url())
                        .enabled(true)
                        .build();

        var resultUpdate =
                mvc.perform(
                                put("/project/{id}", project.getId())
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
