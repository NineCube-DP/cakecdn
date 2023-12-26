/* (C)2023 */
package pl.ninecube.oss.cakecdn.model.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Builder
public class Account {
  Long id;
  String username;
  String password;

  @Setter(AccessLevel.NONE)
  Long version;
}
