/* (C)2023 */
package pl.ninecube.oss.cakecdn.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.validator.constraints.URL;

@Jacksonized
@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectCreateDto {
    String name;
  
    @URL
    String baseUrl;
}
