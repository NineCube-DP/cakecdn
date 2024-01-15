/* (C)2023 */
package pl.ninecube.oss.cakecdn.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import pl.ninecube.oss.cakecdn.BaseIntegrationTest;
import pl.ninecube.oss.cakecdn.MinioIntegration;
import pl.ninecube.oss.cakecdn.model.dto.ProjectCreateDto;
import pl.ninecube.oss.cakecdn.model.dto.ProjectResponse;
import pl.ninecube.oss.cakecdn.model.dto.StorageCreateDto;
import pl.ninecube.oss.cakecdn.model.dto.StorageResponse;
import pl.ninecube.oss.cakecdn.repository.StorageRepository;

import java.util.Locale;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class StorageRestControllerIT extends BaseIntegrationTest implements MinioIntegration {

  private final StorageRepository storageRepository;

  @Test
  public void shouldCreateNewStorage() {
    var response = createSampleStorage();
    assert storageRepository.existsById(response.getId());
  }

  @Test
  public void shouldReturnStorageInfo() throws Exception {
    var sample = createSampleStorage();

    var result =
            mvc.perform(
                            get("/storage/{id}", sample.getId())
                                    .with(httpBasic("test", "password")))
                    .andExpect(status().isOk())
                    .andReturn();

    var content = result.getResponse().getContentAsString();

    var response = objectMapper.readValue(content, StorageResponse.class);

    assert response.getName().equals(sample.getName());
  }

  @Test
  public void shouldDeleteStorage() throws Exception {
    var sample = createSampleStorage();

    mvc.perform(delete("/storage/{id}", sample.getId()).with(httpBasic("test", "password")))
            .andExpect(status().isOk())
            .andReturn();

    assert !storageRepository.existsById(sample.getId());
  }

  @SneakyThrows
  private ProjectResponse createSampleProject() {
    var projectCreateDto =
            ProjectCreateDto.builder()
                    .name(faker.pokemon().name())
                    .baseUrl("https://" + faker.internet().domainName())
                    .build();

    var result =
            mvc.perform(
                            post("/project")
                                    .with(httpBasic("test", "password"))
                                    .content(objectMapper.writeValueAsString(projectCreateDto))
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andReturn();

    var content = result.getResponse().getContentAsString();

    return objectMapper.readValue(content, ProjectResponse.class);
  }

  @SneakyThrows
  private StorageResponse createSampleStorage() {
    var sampleProject = createSampleProject();

    var storageCreateDto =
            StorageCreateDto.builder()
                    .name(faker.lorem().characters(3, 20).toLowerCase(Locale.ROOT))
                    .build();

    var result =
            mvc.perform(
                            post("/storage")
                                    .param("projectId", sampleProject.getId().toString())
                                    .with(httpBasic("test", "password"))
                                    .content(objectMapper.writeValueAsString(storageCreateDto))
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andReturn();

    var content = result.getResponse().getContentAsString();

    return objectMapper.readValue(content, StorageResponse.class);
  }
}
