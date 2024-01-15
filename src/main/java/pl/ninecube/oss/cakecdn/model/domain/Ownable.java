package pl.ninecube.oss.cakecdn.model.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public abstract class Ownable {
    protected Owner owner;
}
