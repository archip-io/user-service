package com.archipio.userservice.unittest.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.quality.Strictness.LENIENT;

import com.archipio.userservice.exception.UserNotFoundException;
import com.archipio.userservice.persistence.entity.User;
import com.archipio.userservice.persistence.repository.UserRepository;
import com.archipio.userservice.service.impl.AdminServiceImpl;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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

  private static Stream<Arguments> provideNullParameters_banAccount() {
    return Stream.of(Arguments.of((String) null));
  }

  private static Stream<Arguments> provideNullParameters_unbanAccount() {
    return Stream.of(Arguments.of((String) null));
  }

  private static Stream<Arguments> provideNullParameters_deleteUserAccount() {
    return Stream.of(Arguments.of((String) null));
  }

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

  @ParameterizedTest
  @MethodSource("provideNullParameters_banAccount")
  public void banAccount_whenParametersIsNull_thenNullPointerException(String username) {
    assertThatNullPointerException().isThrownBy(() -> adminService.banAccount(username));
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

  @Test
  public void unbanAccount_whenUserExists_thenEnableUser() {
    // Prepare
    final var username = "user";

    final var user = new User();
    user.setUsername(username);

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
    when(userRepository.save(user)).thenReturn(user);

    // Do
    adminService.unbanAccount(username);

    // Check
    verify(userRepository, times(1)).findByUsername(username);
    verify(userRepository, times(1)).save(user);

    assertThat(user.getIsEnabled()).isTrue();
  }

  @ParameterizedTest
  @MethodSource("provideNullParameters_unbanAccount")
  public void unbanAccount_whenParametersIsNull_thenNullPointerException(String username) {
    assertThatNullPointerException().isThrownBy(() -> adminService.unbanAccount(username));
  }

  @Test
  public void unbanAccount_whenUserNotExists_thenThrownUserNotFoundException() {
    // Prepare
    final var username = "user";

    final var user = new User();
    user.setUsername(username);

    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    // Do
    assertThatExceptionOfType(UserNotFoundException.class)
        .isThrownBy(() -> adminService.unbanAccount(username));

    // Check
    verify(userRepository, times(1)).findByUsername(username);
  }

  @Test
  public void deleteUserAccount_whenUserExists_thenDeleteUser() {
    // Prepare
    final var username = "user";

    final var user = new User();
    user.setUsername(username);

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
    doNothing().when(userRepository).delete(user);

    // Do
    adminService.deleteUserAccount(username);

    // Check
    verify(userRepository, times(1)).findByUsername(username);
    verify(userRepository, times(1)).delete(user);
  }

  @ParameterizedTest
  @MethodSource("provideNullParameters_deleteUserAccount")
  public void deleteUserAccount_whenParametersIsNull_thenNullPointerException(String username) {
    assertThatNullPointerException().isThrownBy(() -> adminService.deleteUserAccount(username));
  }

  @Test
  public void deleteUserAccount_whenUserNotExists_thenThrownUserNotFoundException() {
    // Prepare
    final var username = "user";

    final var user = new User();
    user.setUsername(username);

    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    // Do
    assertThatExceptionOfType(UserNotFoundException.class)
        .isThrownBy(() -> adminService.deleteUserAccount(username));

    // Check
    verify(userRepository, times(1)).findByUsername(username);
  }
}
