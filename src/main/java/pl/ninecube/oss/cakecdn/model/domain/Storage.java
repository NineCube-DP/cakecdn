/* (C)2023-2024 */
package pl.ninecube.oss.cakecdn.model.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Builder
public class Storage {
    Long id;

    String name;

    Project project;

    List<Item> items;

    @Setter(AccessLevel.NONE)
    Long version;
}
