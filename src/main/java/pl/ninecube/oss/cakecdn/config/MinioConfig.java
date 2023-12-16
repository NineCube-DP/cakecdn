package pl.ninecube.oss.cakecdn.config;

import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan
public class MinioConfig {

    @Bean
    @ConditionalOnProperty(name = "minio.enabled", havingValue = "true")
    MinioClient createClient(MinioProperties properties) {
        return MinioClient.builder()
                .endpoint(properties.getEndpoint())
                .credentials(properties.getCredentials().getAccessKey(), properties.getCredentials().getSecretKey())
                .build();
    }
}
