/* (C)2023 */
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
    public abstract Storage toDomain(StorageCreateDto dto);

    public abstract Storage toDomain(StorageEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract StorageEntity toEntity(Storage account);

    public abstract StorageResponse toResponse(StorageEntity updatedEntity);
}
