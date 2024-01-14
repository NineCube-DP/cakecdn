/* (C)2024 */
package pl.ninecube.oss.cakecdn.model.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationToken {
    String applicationName;
    String token;
    Long projectId;
}
