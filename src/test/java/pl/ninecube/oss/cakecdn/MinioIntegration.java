package pl.ninecube.oss.cakecdn;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

public interface MinioIntegration {
    int MINIO_API_PORT = 9000;
    int MINIO_DASHBOARD_PORT = 9001;
    String MINIO_DOCKER_IMAGE = "minio/minio:latest";

    @Container
    GenericContainer<?> minioContainer = new GenericContainer<>(DockerImageName.parse(MINIO_DOCKER_IMAGE))
            .withExposedPorts(MINIO_API_PORT)
            .withExposedPorts(MINIO_DASHBOARD_PORT)
            .withCommand("server /data --console-address :9001")
            .withReuse(true);

    @DynamicPropertySource
    static void dynamicConfigureMinioProperties(DynamicPropertyRegistry registry) {
        minioContainer.start();

        registry.add("storage.s3.connection.host", minioContainer::getHost);
        registry.add("storage.s3.connection.port", () -> minioContainer.getMappedPort(MINIO_API_PORT)
        );
        registry.add("storage.s3.connection.tls-enabled", () -> false
        );
        registry.add("storage.s3.credential.login", () -> "minioadmin"
        );
        registry.add("storage.s3.credential.password", () -> "minioadmin"
        );
        registry.add("storage.s3.connection.bucket-name", () -> "bucket-testcontainers"
        );
    }
}
