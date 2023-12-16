package pl.ninecube.oss.cakecdn.model.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Value
public class ProjectResponse {
    Long id;
    String name;
    String baseUrl;
    boolean enabled;
}
