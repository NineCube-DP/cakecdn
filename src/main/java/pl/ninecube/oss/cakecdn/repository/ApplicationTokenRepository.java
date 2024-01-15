/* (C)2024 */
package pl.ninecube.oss.cakecdn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ninecube.oss.cakecdn.model.entity.ApplicationTokenEntity;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ApplicationTokenRepository extends JpaRepository<ApplicationTokenEntity, Long> {
    Optional<ApplicationTokenEntity> findByToken(String applicationToken);

    boolean existsByToken(String token);

    Set<ApplicationTokenEntity> findAllByProjectIdAndOwnerId(Long projectId, Long ownerId);

    Optional<ApplicationTokenEntity> findByIdAndOwnerId(Long tokenId, Long ownerId);
}
