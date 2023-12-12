package pl.ninecube.oss.cakecdn.model.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.ninecube.oss.cakecdn.model.dto.OwnerCreateDto;
import pl.ninecube.oss.cakecdn.model.dto.OwnerResponse;
import pl.ninecube.oss.cakecdn.model.entity.OwnerEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class OwnerMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mapping(source = "password", target = "password", qualifiedByName = "encodePassword")
    public abstract OwnerEntity toEntity(OwnerCreateDto dto);

    public abstract OwnerResponse toResponse(OwnerEntity entity);

    @Named("encodePassword")
    public String encodePassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }
}
