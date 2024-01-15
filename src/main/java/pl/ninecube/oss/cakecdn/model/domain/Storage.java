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

    List<Item> items;

    @Setter(AccessLevel.NONE)
    Long version;

    public String getName() {
        return this.name.split("-", 1)[0];
    }

    public String getBucketName() {
        return this.name.toLowerCase();
    }
}
