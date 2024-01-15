/* (C)2023-2024 */
package pl.ninecube.oss.cakecdn.model.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Map;
import java.util.Set;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class Item extends Ownable {
    Long id;
    String fileName;
    String originalFileName;
    Long fileSize;
    String contentType;
    String uuid;
    Long storageId;
    //  Storage storage;
    String url;
    Set<String> tags;
    Set<String> categories;
    Map<String, String> parameters;

    @Setter(AccessLevel.NONE)
    Long version;
}
