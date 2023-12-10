package pl.ninecube.oss.cakecdn;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
@TestPropertySource(
        locations = "classpath:application-integration.yml")
public class BaseIntegrationTest {

    @Autowired
    protected MockMvc mvc;

    static int MINIO_API_PORT = 9000;
    static int MINIO_DASHBOARD_PORT = 9001;
    static String POSTGRES_DOCKER_IMAGE = "postgres:16.1-alpine3.18";

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse(POSTGRES_DOCKER_IMAGE))
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
