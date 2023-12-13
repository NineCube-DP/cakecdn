package pl.ninecube.oss.cakecdn.repository;

import org.springframework.data.repository.CrudRepository;
import pl.ninecube.oss.cakecdn.model.entity.AccountEntity;

public interface AccountRepository extends CrudRepository<AccountEntity, Long> {
}
