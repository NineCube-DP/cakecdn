/* (C)2023 */
package pl.ninecube.oss.cakecdn;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

public interface MinioIntegration {
  int MINIO_API_PORT = 9000;
  String MINIO_DOCKER_IMAGE = "minio/minio:latest";

  @Container
  GenericContainer<?> minioContainer =
          new GenericContainer<>(DockerImageName.parse(MINIO_DOCKER_IMAGE))
                  .withExposedPorts(MINIO_API_PORT)
                  .withEnv("MINIO_ROOT_USER", "minioadmin")
                  .withEnv("MINIO_ROOT_PASSWORD", "minioadmin")
                  .withCommand("server /data")
                  .withReuse(true);

  @DynamicPropertySource
  static void dynamicConfigureMinioProperties(DynamicPropertyRegistry registry) {
    minioContainer.start();

    registry.add("minio.enabled", () -> true);
    registry.add(
            "minio.endpoint",
            () ->
                    "http://"
                            + minioContainer.getHost()
                            + ":"
                            + minioContainer.getMappedPort(MINIO_API_PORT));
    registry.add("minio.credentials.accessKey", () -> "minioadmin");
    registry.add("minio.credentials.secretKey", () -> "minioadmin");
  }
}
