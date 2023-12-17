package pl.ninecube.oss.cakecdn.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.ninecube.oss.cakecdn.model.entity.StorageEntity;

import java.util.Optional;

@Repository
public interface StorageRepository extends CrudRepository<StorageEntity, Long> {
    Optional<StorageEntity> findByName(String bucketName);
}
