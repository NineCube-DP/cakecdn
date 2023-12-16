package pl.ninecube.oss.cakecdn.model.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.Set;

@Data
@Builder
public class Bucket {
    String name;

    @Setter(AccessLevel.NONE)
    Set<Storage> files;
}
