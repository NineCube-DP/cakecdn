/* (C)2023 */
package pl.ninecube.oss.cakecdn;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.ninecube.oss.cakecdn.repository.StorageRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class CakeCdnApplicationTests extends BaseIntegrationTest {

  private final StorageRepository storageRepository;

  @Test
  void contextLoads() {
    assertThat(storageRepository).isNotNull();
  }
}
