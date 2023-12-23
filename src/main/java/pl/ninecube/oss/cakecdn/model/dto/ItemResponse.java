/* (C)2023 */
package pl.ninecube.oss.cakecdn.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;
import java.util.Set;

@Value
@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemResponse {
  Long id;

  String fileName;
  String originalFileName;
  String contentType;
  long fileSize;

  String uuid;
  String url;

  Set<String> tags;
  Set<String> categories;
  Map<String, String> parameters;
}
