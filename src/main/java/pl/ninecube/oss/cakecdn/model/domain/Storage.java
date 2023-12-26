/* (C)2023 */
package pl.ninecube.oss.cakecdn.model.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Builder
public class Storage {
  Long id;

  String name;

  Project project;

  @Setter(AccessLevel.NONE)
  Long version;
}
