package pl.ninecube.oss.cakecdn.model.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.ninecube.oss.cakecdn.model.domain.Owner;
import pl.ninecube.oss.cakecdn.model.dto.OwnerCreateDto;
import pl.ninecube.oss.cakecdn.model.dto.OwnerResponse;
import pl.ninecube.oss.cakecdn.model.dto.OwnerUpdateDto;
import pl.ninecube.oss.cakecdn.model.entity.OwnerEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public abstract class OwnerMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mapping(ignore = true, target = "id")
    @Mapping(source = "password", target = "password", qualifiedByName = "encodePassword")
    public abstract Owner toDomain(OwnerCreateDto dto);

    public abstract Owner toDomain(OwnerEntity entity);

    public abstract OwnerEntity toEntity(Owner owner);

    public abstract OwnerResponse toResponse(OwnerEntity entity);

    @Mapping(ignore = true, target = "id")
    public abstract OwnerResponse toResponse(Owner owner);

    @Mapping(ignore = true, target = "id")
    @BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "password", target = "password", qualifiedByName = "encodePassword")
    public abstract Owner update(@MappingTarget Owner owner, OwnerUpdateDto dto);

    @Named("encodePassword")
    public String encodePassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }
}
