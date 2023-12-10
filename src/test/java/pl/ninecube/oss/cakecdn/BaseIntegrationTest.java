package pl.ninecube.oss.cakecdn;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

public interface BaseIntegrationTest {


    int MINIO_API_PORT = 9000;
    int MINIO_DASHBOARD_PORT = 9001;
    String POSTGRES_DOCKER_IMAGE = "postgres:16.1-alpine3.18";

    @Container
    PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse(POSTGRES_DOCKER_IMAGE))
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
