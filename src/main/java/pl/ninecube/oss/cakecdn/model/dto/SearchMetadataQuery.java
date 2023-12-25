/* (C)2023 */
package pl.ninecube.oss.cakecdn.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.Set;

@Value
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchMetadataQuery {

    Set<String> tags;
    Set<String> categories;
    Set<String> parameters;
}
