package com.archipio.userservice.service.impl;

import com.archipio.userservice.dto.CredentialsInputDto;
import com.archipio.userservice.dto.CredentialsOutputDto;
import com.archipio.userservice.exception.EmailAlreadyExistsException;
import com.archipio.userservice.exception.RoleNotFoundException;
import com.archipio.userservice.exception.UserNotFoundException;
import com.archipio.userservice.exception.UsernameAlreadyExistsException;
import com.archipio.userservice.mapper.UserMapper;
import com.archipio.userservice.persistence.entity.Role;
import com.archipio.userservice.persistence.repository.RoleRepository;
import com.archipio.userservice.persistence.repository.UserRepository;
import com.archipio.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private static final String DEFAULT_ROLE_NAME = "USER";

  private static Role defaultRole;

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final UserMapper userMapper;
  private final BCryptPasswordEncoder passwordEncoder;

  @Override
  public void saveCredentials(CredentialsInputDto credentialsInputDto) {
    if (userRepository.existsByUsername(credentialsInputDto.getUsername())) {
      throw new UsernameAlreadyExistsException();
    }
    if (userRepository.existsByEmail(credentialsInputDto.getEmail())) {
      throw new EmailAlreadyExistsException();
    }

    credentialsInputDto.setPassword(passwordEncoder.encode(credentialsInputDto.getPassword()));
    var user = userMapper.toEntity(credentialsInputDto);
    user.setRole(getDefaultRole());
    userRepository.save(user);

    // TODO: Создать событие создания пользователя в Kafka
  }

  @Override
  public CredentialsOutputDto findByLogin(String login) {
    var user = userRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
    return userMapper.toDto(user);
  }

  @Override
  public CredentialsOutputDto findByUsernameAndEmail(String username, String email) {
    var user = userRepository.findByUsernameAndEmail(username, email).orElseThrow(UserNotFoundException::new);
    return userMapper.toDto(user);
  }

  private Role getDefaultRole() {
    if (defaultRole == null) {
      defaultRole =
          roleRepository.findByName(DEFAULT_ROLE_NAME).orElseThrow(RoleNotFoundException::new);
    }
    return defaultRole;
  }
}
