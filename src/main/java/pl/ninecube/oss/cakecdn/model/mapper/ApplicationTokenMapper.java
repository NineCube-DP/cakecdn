/* (C)2024 */
package pl.ninecube.oss.cakecdn.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import pl.ninecube.oss.cakecdn.model.domain.ApplicationToken;
import pl.ninecube.oss.cakecdn.model.dto.TokenResponse;
import pl.ninecube.oss.cakecdn.model.entity.ApplicationTokenEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class ApplicationTokenMapper {

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "version")
    @Mapping(source = "owner.id", target = "ownerId")
    public abstract ApplicationTokenEntity toEntity(ApplicationToken applicationToken);

    public abstract TokenResponse toResponse(ApplicationTokenEntity applicationTokenEntity);
}
