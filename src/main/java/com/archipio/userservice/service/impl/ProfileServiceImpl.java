package com.archipio.userservice.service.impl;

import com.archipio.userservice.dto.ProfileDto;
import com.archipio.userservice.exception.UserNotFoundException;
import com.archipio.userservice.mapper.UserMapper;
import com.archipio.userservice.persistence.repository.UserRepository;
import com.archipio.userservice.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public ProfileDto getProfileByUsername(String username) {
    var user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    return userMapper.toProfile(user);
  }
}
