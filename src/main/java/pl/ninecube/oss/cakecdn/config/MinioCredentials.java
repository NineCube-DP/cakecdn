package pl.ninecube.oss.cakecdn.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MinioCredentials {
  private final String accessKey;
  private final String secretKey;
}
