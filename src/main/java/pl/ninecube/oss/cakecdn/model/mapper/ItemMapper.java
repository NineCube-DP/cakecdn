package pl.ninecube.oss.cakecdn.model.mapper;

import org.mapstruct.*;
import pl.ninecube.oss.cakecdn.model.domain.Item;
import pl.ninecube.oss.cakecdn.model.dto.ItemResponse;
import pl.ninecube.oss.cakecdn.model.entity.ItemEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = StorageMapper.class
)
public abstract class ItemMapper {
    public abstract Item toDomain(ItemEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract ItemEntity toEntity(Item item);

    @Mapping(source = ".", target = "fileName", qualifiedByName = "itemNameMapper")
    public abstract ItemResponse toResponse(ItemEntity item);

    @Named("itemNameMapper")
    public String mapName(ItemEntity item) {
        if (item.getFileName() == null || item.getFileName().isBlank()) {
            return item.getOriginalFileName();
        } else {
            return item.getFileName();
        }
    }
}
