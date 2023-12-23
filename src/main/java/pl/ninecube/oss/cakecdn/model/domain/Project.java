package pl.ninecube.oss.cakecdn.model.domain;

import lombok.Data;

@Data
public class Project {
  Long id;
  Account owner;

  String name;
  String baseUrl;
  boolean enabled;
}
