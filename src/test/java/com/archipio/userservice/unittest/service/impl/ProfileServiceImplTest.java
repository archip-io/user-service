package com.archipio.userservice.unittest.service.impl;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.quality.Strictness.LENIENT;

import com.archipio.userservice.dto.ProfileDto;
import com.archipio.userservice.exception.UserNotFoundException;
import com.archipio.userservice.mapper.UserMapper;
import com.archipio.userservice.persistence.entity.User;
import com.archipio.userservice.persistence.repository.UserRepository;
import com.archipio.userservice.service.impl.ProfileServiceImpl;
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
class ProfileServiceImplTest {

  @Mock private UserRepository userRepository;
  @Mock private UserMapper userMapper;
  @InjectMocks private ProfileServiceImpl profileService;

  @Test
  public void getProfileByUsername_whenUserExists_thenHisProfile() {
    // Prepare
    final var username = "user";
    final var email = "email";
    final var isEnabled = true;
    final var profileDto =
            ProfileDto.builder().username(username).email(email).isEnabled(isEnabled).build();

    final var user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setIsEnabled(isEnabled);

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
    when(userMapper.toProfile(user)).thenReturn(profileDto);

    // Do
    profileService.getProfileByUsername(username);

    // Check
    verify(userRepository, times(1)).findByUsername(username);
    verify(userMapper, times(1)).toProfile(user);
  }

  @Test
  public void getProfileByUsername_whenUserNotExists_thenThrowUserNotFoundException() {
    // Prepare
    final var username = "user";

    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    // Do
    assertThatExceptionOfType(UserNotFoundException.class).isThrownBy(() -> profileService.getProfileByUsername(username));

    // Check
    verify(userRepository, times(1)).findByUsername(username);
  }
}
