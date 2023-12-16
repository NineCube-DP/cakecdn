package pl.ninecube.oss.cakecdn.model.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
@Builder
public class Storage {
    String fileName;
    String originalFilename;
    String contentType;
    String uuid;
    Set<String> tags;
    Set<String> categories;
    Map<String, String> parameters;
}
