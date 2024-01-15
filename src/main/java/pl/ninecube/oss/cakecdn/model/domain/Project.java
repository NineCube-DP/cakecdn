/* (C)2023 */
package pl.ninecube.oss.cakecdn.model.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class Project extends Ownable {
    Long id;

    String name;
    String baseUrl;
    boolean enabled;

    @Setter(AccessLevel.NONE)
    Long version;
}
