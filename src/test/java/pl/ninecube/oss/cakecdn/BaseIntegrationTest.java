/* (C)2023 */
package pl.ninecube.oss.cakecdn;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.MinioClient;
import net.datafaker.Faker;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = CakeCdnApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integration.yml")
@ActiveProfiles("integration")
public abstract class BaseIntegrationTest {
  @Autowired
  protected MockMvc mvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @MockBean
  MinioClient client;

  protected static final Faker faker = new Faker();
  private static final String POSTGRES_DOCKER_IMAGE = "postgres:16.1-alpine3.18";

  @Container
  static PostgreSQLContainer<?> postgreSQLContainer =
          new PostgreSQLContainer<>(DockerImageName.parse(POSTGRES_DOCKER_IMAGE))
                  .withDatabaseName("testcontainer-db")
                  .withUsername("sa")
                  .withPassword("sa")
                  .withReuse(true);

  @DynamicPropertySource
  static void dynamicConfigureMinioProperties(DynamicPropertyRegistry registry) {
    postgreSQLContainer.start();

    registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
  }
}
