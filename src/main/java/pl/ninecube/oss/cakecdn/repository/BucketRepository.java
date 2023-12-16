package pl.ninecube.oss.cakecdn.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.ninecube.oss.cakecdn.model.entity.BucketEntity;

import java.util.Optional;

@Repository
public interface BucketRepository extends CrudRepository<BucketEntity, Long> {
    Optional<BucketEntity> findByName(String bucketName);
}
