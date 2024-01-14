/* (C)2023-2024 */
package pl.ninecube.oss.cakecdn.model.mapper;

import org.mapstruct.*;
import pl.ninecube.oss.cakecdn.model.domain.Storage;
import pl.ninecube.oss.cakecdn.model.dto.StorageCreateDto;
import pl.ninecube.oss.cakecdn.model.dto.StorageResponse;
import pl.ninecube.oss.cakecdn.model.entity.StorageEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {ItemMapper.class, AccountMapper.class})
public abstract class StorageMapper {
    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "project")
    @Mapping(ignore = true, target = "version")
    @Mapping(ignore = true, target = "items")
    public abstract Storage toDomain(StorageCreateDto dto);

    @Mapping(ignore = true, target = "items")
    public abstract Storage toDomain(StorageEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract StorageEntity toEntity(Storage storage);

    @Mapping(ignore = true, target = "items")
    public abstract StorageResponse toResponse(StorageEntity updatedEntity);

    public abstract StorageResponse toResponse(Storage storage);
}
