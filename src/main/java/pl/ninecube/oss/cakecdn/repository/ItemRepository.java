/* (C)2023-2024 */
package pl.ninecube.oss.cakecdn.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.ninecube.oss.cakecdn.model.entity.ItemEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ItemRepository extends CrudRepository<ItemEntity, Long> {
    Optional<ItemEntity> findByStorageIdAndUuidAndOwnerId(Long storageId, String itemUuid, Long id);

    List<ItemEntity> findByTagsInAndCategoriesInAndParametersInAndOwnerId(
            Set<String> tags, Set<String> categories, Set<String> parameters, Long id);

    List<ItemEntity> findByStorageId(Long storageId);

    Optional<ItemEntity> findByIdAndOwnerId(Long itemId, Long ownerId);
}
