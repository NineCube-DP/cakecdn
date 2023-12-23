/* (C)2023 */
package pl.ninecube.oss.cakecdn.model.domain;

import lombok.Data;

@Data
public class Account {
  Long id;
  String username;
  String password;
}
