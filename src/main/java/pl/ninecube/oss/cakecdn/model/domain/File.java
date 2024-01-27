package pl.ninecube.oss.cakecdn.model.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class File {
    byte[] payload;
    String fileName;
}
