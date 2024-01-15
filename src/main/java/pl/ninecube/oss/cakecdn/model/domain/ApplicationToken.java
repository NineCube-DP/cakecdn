/* (C)2024 */
package pl.ninecube.oss.cakecdn.model.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class ApplicationToken extends Ownable {
    String applicationName;
    String token;
    Long projectId;
}
