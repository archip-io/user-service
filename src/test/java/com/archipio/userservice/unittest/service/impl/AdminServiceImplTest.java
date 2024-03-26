package com.archipio.userservice.unittest.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.quality.Strictness.LENIENT;

import com.archipio.userservice.exception.UserNotFoundException;
import com.archipio.userservice.persistence.entity.User;
import com.archipio.userservice.persistence.repository.UserRepository;
import com.archipio.userservice.service.impl.AdminServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = LENIENT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdminServiceImplTest {

  @Mock private UserRepository userRepository;
  @InjectMocks private AdminServiceImpl adminService;

  @Test
  public void banAccount_whenUserExists_thenDisableUser() {
    // Prepare
    final var username = "user";

    final var user = new User();
    user.setUsername(username);

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
    when(userRepository.save(user)).thenReturn(user);

    // Do
    adminService.banAccount(username);

    // Check
    verify(userRepository, times(1)).findByUsername(username);
    verify(userRepository, times(1)).save(user);

    assertThat(user.getIsEnabled()).isFalse();
  }

  @Test
  public void banAccount_whenUserNotExists_thenThrownUserNotFoundException() {
    // Prepare
    final var username = "user";

    final var user = new User();
    user.setUsername(username);

    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    // Do
    assertThatExceptionOfType(UserNotFoundException.class)
        .isThrownBy(() -> adminService.banAccount(username));

    // Check
    verify(userRepository, times(1)).findByUsername(username);
  }
}
