package pl.ninecube.oss.cakecdn.model.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.ninecube.oss.cakecdn.model.domain.Account;
import pl.ninecube.oss.cakecdn.model.dto.AccountCreateDto;
import pl.ninecube.oss.cakecdn.model.dto.AccountResponse;
import pl.ninecube.oss.cakecdn.model.dto.AccountUpdateDto;
import pl.ninecube.oss.cakecdn.model.entity.AccountEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class AccountMapper {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Mapping(ignore = true, target = "id")
  @Mapping(source = "password", target = "password", qualifiedByName = "encodePassword")
  public abstract Account toDomain(AccountCreateDto dto);

  public abstract Account toDomain(AccountEntity entity);

  @Mapping(ignore = true, target = "projects")
  public abstract AccountEntity toEntity(Account account);

  public abstract AccountResponse toResponse(AccountEntity entity);

  @Mapping(ignore = true, target = "id")
  public abstract AccountResponse toResponse(Account account);

  @Mapping(ignore = true, target = "id")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(source = "password", target = "password", qualifiedByName = "encodePassword")
  public abstract Account update(@MappingTarget Account account, AccountUpdateDto dto);

  @Named("encodePassword")
  public String encodePassword(String plainPassword) {
    return passwordEncoder.encode(plainPassword);
  }
}
