package pl.ninecube.oss.cakecdn.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.ninecube.oss.cakecdn.model.entity.StorageEntity;

@Repository
public interface StorageRepository extends CrudRepository<StorageEntity, Long> {
}
