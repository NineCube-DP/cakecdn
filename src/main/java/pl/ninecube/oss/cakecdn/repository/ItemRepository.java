/* (C)2023 */
package pl.ninecube.oss.cakecdn.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.ninecube.oss.cakecdn.model.entity.ItemEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ItemRepository extends CrudRepository<ItemEntity, Long> {
    Optional<ItemEntity> findByStorageNameAndUuid(String storageName, String itemUuid);

    List<ItemEntity> findByTagsInAndCategoriesInAndParametersIn(Set<String> tags, Set<String> categories, Set<String> parameters);
}
