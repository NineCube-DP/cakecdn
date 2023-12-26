/* (C)2023 */
package pl.ninecube.oss.cakecdn.model.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Builder
public class Project {
  Long id;
  Account owner;

  String name;
  String baseUrl;
  boolean enabled;

  @Setter(AccessLevel.NONE)
  Long version;
}
