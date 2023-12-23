/* (C)2023 */
package pl.ninecube.oss.cakecdn.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;
import java.util.Set;

@Jacksonized
@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemUpdateDto {
  String fileName;

  Set<String> tags;
  Set<String> categories;
  Map<String, String> parameters;
}
