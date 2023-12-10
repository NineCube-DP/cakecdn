package pl.ninecube.oss.cakecdn.repository;

import org.springframework.data.repository.CrudRepository;
import pl.ninecube.oss.cakecdn.model.entity.OwnerEntity;

public interface OwnerRepository extends CrudRepository<OwnerEntity, Long> {
}
