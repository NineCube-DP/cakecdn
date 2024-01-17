package pl.ninecube.oss.cakecdn.repository.query;

import com.querydsl.core.types.dsl.Expressions;
import org.springframework.stereotype.Service;
import pl.ninecube.oss.cakecdn.model.domain.Owner;
import pl.ninecube.oss.cakecdn.model.dto.SearchMetadataQuery;
import pl.ninecube.oss.cakecdn.model.entity.ItemEntity;

import java.util.List;

@Service
public class ItemQuery extends AbstractQuery {
    public List<ItemEntity> findItemsByParameters(SearchMetadataQuery params, Owner owner) {
        return queryFactory().selectFrom(itemEntity)
                .where(
                        itemEntity.fileName.containsIgnoreCase(params.getFileName()).and(
                                Expressions.allOf(
                                        itemEntity.tags.any().in(params.getTags()),
                                        itemEntity.categories.any().in(params.getCategories()),
                                        itemEntity.ownerId.eq(owner.getId())
                                )
                        )
                )
                .fetch();
    }
}
