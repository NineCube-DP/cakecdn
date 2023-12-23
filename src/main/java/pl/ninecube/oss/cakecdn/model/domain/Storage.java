/* (C)2023 */
package pl.ninecube.oss.cakecdn.model.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Storage {
  Long id;

  String name;

  Project project;

  //    @Setter(AccessLevel.NONE)
  //    Set<Item> files;
}
