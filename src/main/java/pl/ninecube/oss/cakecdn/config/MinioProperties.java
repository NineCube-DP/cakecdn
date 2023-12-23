/* (C)2023 */
package pl.ninecube.oss.cakecdn.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
  private final String endpoint;
  private final boolean enabled;
  private final MinioCredentials credentials;
}
