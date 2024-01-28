/* (C)2023-2024 */
package pl.ninecube.oss.cakecdn.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.ninecube.oss.cakecdn.model.entity.ProjectEntity;

@Repository
public interface ProjectRepository extends CrudRepository<ProjectEntity, Long> {
    List<ProjectEntity> findAllByNameContainingIgnoreCaseAndOwnerId(String name, Long ownerId);

    Optional<ProjectEntity> findByIdAndOwnerId(Long projectId, Long ownerId);

    void deleteByIdAndOwnerId(Long projectId, Long ownerId);

    List<ProjectEntity> findAllByOwnerId(Long ownerId);
}
