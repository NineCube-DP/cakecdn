/* (C)2023-2024 */
package pl.ninecube.oss.cakecdn.model.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@Data
@Builder
public class Item {
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
