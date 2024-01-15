/* (C)2023-2024 */
package pl.ninecube.oss.cakecdn.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.ninecube.oss.cakecdn.model.entity.StorageEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface StorageRepository extends CrudRepository<StorageEntity, Long> {
    Optional<StorageEntity> findByNameAndOwnerId(String bucketName, Long ownerId);

    List<StorageEntity> findAllByNameContainingIgnoreCaseAndOwnerId(String storageName, Long ownerId);

    List<StorageEntity> findByProjectId(Long projectId);

    Optional<StorageEntity> findByIdAndOwnerId(Long bucketId, Long ownerId);
}
