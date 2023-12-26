/* (C)2023 */
package pl.ninecube.oss.cakecdn.model.mapper;

import org.mapstruct.*;
import pl.ninecube.oss.cakecdn.model.domain.Item;
import pl.ninecube.oss.cakecdn.model.dto.ItemResponse;
import pl.ninecube.oss.cakecdn.model.dto.ItemUpdateDto;
import pl.ninecube.oss.cakecdn.model.entity.ItemEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = StorageMapper.class)
public abstract class ItemMapper {
  public abstract Item toDomain(ItemEntity entity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  public abstract ItemEntity toEntity(Item item);

  @Mapping(source = ".", target = "fileName", qualifiedByName = "itemNameMapper")
  public abstract ItemResponse toResponse(ItemEntity item);

  @Mapping(ignore = true, target = "id")
  @Mapping(ignore = true, target = "originalFileName")
  @Mapping(ignore = true, target = "fileSize")
  @Mapping(ignore = true, target = "contentType")
  @Mapping(ignore = true, target = "uuid")
  @Mapping(ignore = true, target = "storage")
  @Mapping(ignore = true, target = "url")
  @Mapping(ignore = true, target = "version")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  public abstract Item update(@MappingTarget Item file, ItemUpdateDto dto);

  @Named("itemNameMapper")
  public String mapName(ItemEntity item) {
    if (item.getFileName() == null || item.getFileName().isBlank()) {
      return item.getOriginalFileName();
    } else {
      return item.getFileName();
    }
  }
}
