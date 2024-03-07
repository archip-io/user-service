package com.archipio.userservice.mapper;

import com.archipio.userservice.dto.CredentialsInputDto;
import com.archipio.userservice.dto.CredentialsOutputDto;
import com.archipio.userservice.persistence.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

  User toEntity(CredentialsInputDto credentialsInputDto);

  CredentialsOutputDto toDto(User user);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  User partialUpdate(CredentialsInputDto credentialsInputDto, @MappingTarget User user);
}
