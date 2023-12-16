package pl.ninecube.oss.cakecdn.model.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import pl.ninecube.oss.cakecdn.model.domain.Storage;

import java.util.Set;

@Getter
@Entity
public class BucketEntity {
    String name;
    Set<Storage> files;
}
