package pl.ninecube.oss.cakecdn.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.ninecube.oss.cakecdn.model.entity.ItemEntity;

import java.util.Optional;

@Repository
public interface ItemRepository extends CrudRepository<ItemEntity, Long> {
  Optional<ItemEntity> findByStorageNameAndUuid(String storageName, String itemUuid);
}
