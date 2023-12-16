package pl.ninecube.oss.cakecdn.model.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import pl.ninecube.oss.cakecdn.model.domain.Project;
import pl.ninecube.oss.cakecdn.model.dto.ProjectCreateDto;
import pl.ninecube.oss.cakecdn.model.dto.ProjectResponse;
import pl.ninecube.oss.cakecdn.model.dto.ProjectUpdateDto;
import pl.ninecube.oss.cakecdn.model.entity.ProjectEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {AccountMapper.class}
)
public abstract class ProjectMapper {

    @Autowired
    AccountMapper accountMapper;

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "enabled")
    @Mapping(ignore = true, target = "owner")
    public abstract Project toDomain(ProjectCreateDto dto);

    public abstract Project toDomain(ProjectEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract ProjectEntity toEntity(Project account);

    public abstract ProjectResponse toResponse(ProjectEntity updatedEntity);

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "owner")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Project update(@MappingTarget Project domain, ProjectUpdateDto dto);
}
