/* (C)2023-2024 */
package pl.ninecube.oss.cakecdn.model.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class Storage extends Ownable {
    Long id;
    Long projectId;

    String name;
    String bucketName;

    List<Item> items;

    @Setter(AccessLevel.NONE)
    Long version;
}
