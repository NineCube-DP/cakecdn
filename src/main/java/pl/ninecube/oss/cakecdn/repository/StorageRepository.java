/* (C)2023-2024 */
package pl.ninecube.oss.cakecdn.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.ninecube.oss.cakecdn.model.entity.StorageEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface StorageRepository extends CrudRepository<StorageEntity, Long> {
    Optional<StorageEntity> findByName(String bucketName);

    List<StorageEntity> findAllByNameContainingIgnoreCase(String storageName);

    List<StorageEntity> findByProjectId(Long projectId);
}
