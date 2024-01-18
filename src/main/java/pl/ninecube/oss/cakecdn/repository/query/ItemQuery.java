/* (C)2024 */
package pl.ninecube.oss.cakecdn.repository.query;

import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import pl.ninecube.oss.cakecdn.model.domain.Owner;
import pl.ninecube.oss.cakecdn.model.dto.SearchMetadataQuery;
import pl.ninecube.oss.cakecdn.model.entity.ItemEntity;

import java.util.List;

@Service
public class ItemQuery extends AbstractQuery {
    public List<ItemEntity> findItemsByParameters(SearchMetadataQuery params, Owner owner) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (StringUtils.hasText(params.getFileName()))
            booleanBuilder.and(itemEntity.fileName.containsIgnoreCase(params.getFileName()));

        if (!CollectionUtils.isEmpty(params.getTags()))
            booleanBuilder.and(itemEntity.tags.any().in(params.getTags()));

        if (!CollectionUtils.isEmpty(params.getCategories()))
            booleanBuilder.and(itemEntity.categories.any().in(params.getCategories()));

        if (!CollectionUtils.isEmpty(params.getParameters()))
            params.getParameters()
                    .forEach(param -> booleanBuilder.and(itemEntity.parameters.containsKey(param)));

        return queryFactory()
                .selectFrom(itemEntity)
                .where(itemEntity.ownerId.eq(owner.getId()).and(booleanBuilder))
                .fetch();
    }
}
