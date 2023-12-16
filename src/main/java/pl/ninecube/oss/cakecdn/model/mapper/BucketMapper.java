package pl.ninecube.oss.cakecdn.model.mapper;


import org.mapstruct.*;
import pl.ninecube.oss.cakecdn.model.domain.Bucket;
import pl.ninecube.oss.cakecdn.model.dto.BucketCreateDto;
import pl.ninecube.oss.cakecdn.model.dto.BucketResponse;
import pl.ninecube.oss.cakecdn.model.entity.BucketEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public abstract class BucketMapper {
    @Mapping(ignore = true, target = "files")
    public abstract Bucket toDomain(BucketCreateDto dto);

    public abstract Bucket toDomain(BucketEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract BucketEntity toEntity(Bucket account);

    public abstract BucketResponse toResponse(BucketEntity updatedEntity);
}
