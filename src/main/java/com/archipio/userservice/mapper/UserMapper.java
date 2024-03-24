package com.archipio.userservice.mapper;

import com.archipio.userservice.dto.CredentialsInputDto;
import com.archipio.userservice.dto.CredentialsOutputDto;
import com.archipio.userservice.dto.ProfileDto;
import com.archipio.userservice.persistence.entity.Authority;
import com.archipio.userservice.persistence.entity.User;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

  User toEntity(CredentialsInputDto credentialsInputDto);

  @Mapping(
      source = "role.authorities",
      target = "authorities",
      qualifiedByName = "mapAuthoritySetToStringSet")
  CredentialsOutputDto toCredentialsOutput(User user);

  ProfileDto toProfile(User user);

  @Named("mapAuthoritySetToStringSet")
  default Set<String> mapAuthoritySetToStringSet(Set<Authority> authorities) {
    return authorities.stream().map(Authority::getName).collect(Collectors.toSet());
  }

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  User partialUpdate(CredentialsInputDto credentialsInputDto, @MappingTarget User user);
}
