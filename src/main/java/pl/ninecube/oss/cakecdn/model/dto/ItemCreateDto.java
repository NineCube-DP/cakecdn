/* (C)2023-2024 */
package pl.ninecube.oss.cakecdn.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;
import java.util.Set;

@Value
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemCreateDto {
    String fileName;

    Set<String> tags;
    Set<String> categories;
    Map<String, String> parameters;
}
