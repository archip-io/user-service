package com.archipio.userservice.service.impl;

import com.archipio.userservice.exception.UserNotFoundException;
import com.archipio.userservice.persistence.repository.UserRepository;
import com.archipio.userservice.service.AdminService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final UserRepository userRepository;

  @Transactional
  @Override
  public void banAccount(@NonNull String username) {
    var user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

    user.setIsEnabled(false);
    userRepository.save(user);
  }

  @Transactional
  @Override
  public void unbanAccount(@NonNull String username) {
    var user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

    user.setIsEnabled(true);
    userRepository.save(user);
  }

  @Transactional
  @Override
  public void deleteUserAccount(@NonNull String username) {
    var user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    userRepository.delete(user);
  }
}
