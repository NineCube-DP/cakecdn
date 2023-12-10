package pl.ninecube.oss.cakecdn.model.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Value
public class OwnerResponse {
    Long id;
    String username;
}
