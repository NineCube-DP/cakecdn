/* (C)2023-2024 */
package pl.ninecube.oss.cakecdn.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.ninecube.oss.cakecdn.model.entity.StorageEntity;

@Repository
public interface StorageRepository extends CrudRepository<StorageEntity, Long> {
    Optional<StorageEntity> findByNameAndOwnerId(String bucketName, Long ownerId);

    List<StorageEntity> findAllByNameContainingIgnoreCaseAndOwnerId(
            String storageName, Long ownerId);

    List<StorageEntity> findByProjectId(Long projectId);

    Optional<StorageEntity> findByIdAndOwnerId(Long bucketId, Long ownerId);

    Optional<StorageEntity> findByName(String storageName);

    boolean existsByBucketName(String bucketName);

    List<StorageEntity> findAllByOwnerId(Long ownerId);
}
