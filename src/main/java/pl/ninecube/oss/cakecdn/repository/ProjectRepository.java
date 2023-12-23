/* (C)2023 */
package pl.ninecube.oss.cakecdn.repository;

import org.springframework.data.repository.CrudRepository;
import pl.ninecube.oss.cakecdn.model.entity.ProjectEntity;

public interface ProjectRepository extends CrudRepository<ProjectEntity, Long> {
}
