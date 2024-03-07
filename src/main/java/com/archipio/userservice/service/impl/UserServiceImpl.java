package com.archipio.userservice.service.impl;

import com.archipio.userservice.dto.UserDto;
import com.archipio.userservice.exception.EmailAlreadyExistsException;
import com.archipio.userservice.exception.RoleNotFoundException;
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
  public void createUser(UserDto userDto) {
    if (userRepository.existsByUsername(userDto.getUsername())) {
      throw new UsernameAlreadyExistsException();
    }
    if (userRepository.existsByEmail(userDto.getEmail())) {
      throw new EmailAlreadyExistsException();
    }

    userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
    var user = userMapper.toEntity(userDto);
    user.setRole(getDefaultRole());
    userRepository.save(user);

    // TODO: Создать событие создания пользователя в Kafka
  }

  private Role getDefaultRole() {
    if (defaultRole == null) {
      defaultRole =
          roleRepository.findByName(DEFAULT_ROLE_NAME).orElseThrow(RoleNotFoundException::new);
    }
    return defaultRole;
  }
}
