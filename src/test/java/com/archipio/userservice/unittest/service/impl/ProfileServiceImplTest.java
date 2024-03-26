package com.archipio.userservice.unittest.service.impl;

import static com.archipio.userservice.util.CacheUtils.UPDATE_EMAIL_CACHE_TTL_S;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.quality.Strictness.LENIENT;

import com.archipio.userservice.dto.ProfileDto;
import com.archipio.userservice.exception.BadOldPasswordException;
import com.archipio.userservice.exception.EmailAlreadyExistsException;
import com.archipio.userservice.exception.InvalidOrExpiredConfirmationTokenException;
import com.archipio.userservice.exception.UserNotFoundException;
import com.archipio.userservice.exception.UsernameAlreadyExistsException;
import com.archipio.userservice.mapper.UserMapper;
import com.archipio.userservice.persistence.entity.User;
import com.archipio.userservice.persistence.repository.UserRepository;
import com.archipio.userservice.service.impl.ProfileServiceImpl;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = LENIENT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProfileServiceImplTest {

  @Mock private UserRepository userRepository;
  @Mock private UserMapper userMapper;
  @Mock private RedisTemplate<String, ProfileServiceImpl.UpdateEmailCache> redisTemplate;
  @Mock private PasswordEncoder passwordEncoder;
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
    assertThatExceptionOfType(UserNotFoundException.class)
        .isThrownBy(() -> profileService.getProfileByUsername(username));

    // Check
    verify(userRepository, times(1)).findByUsername(username);
  }

  @Test
  public void updateUsername_whenUserExistsAndNewUsernameIsFree_thenUpdateUsername() {
    // Prepare
    final var username = "user";
    final var newUsername = "new_user";

    final var user = new User();
    user.setUsername(username);

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
    when(userRepository.existsByUsername(newUsername)).thenReturn(false);
    when(userRepository.save(user)).thenReturn(user);

    // Do
    profileService.updateUsername(username, newUsername);

    // Check
    verify(userRepository, times(1)).findByUsername(username);
    verify(userRepository, times(1)).existsByUsername(newUsername);
    verify(userRepository, times(1)).save(user);
  }

  @Test
  public void updateUsername_whenUserNotExists_thenThrownUserNotFoundException() {
    // Prepare
    final var username = "user";
    final var newUsername = "new_user";

    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    // Do
    assertThatExceptionOfType(UserNotFoundException.class)
        .isThrownBy(() -> profileService.updateUsername(username, newUsername));

    // Check
    verify(userRepository, times(1)).findByUsername(username);
  }

  @Test
  public void
      updateUsername_whenUserExistsAndUsernameAlreadyExists_thenThrownUsernameAlreadyExistsException() {
    // Prepare
    final var username = "user";
    final var newUsername = "new_user";

    final var user = new User();
    user.setUsername(username);

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
    when(userRepository.existsByUsername(newUsername)).thenReturn(true);

    // Do
    assertThatExceptionOfType(UsernameAlreadyExistsException.class)
        .isThrownBy(() -> profileService.updateUsername(username, newUsername));

    // Check
    verify(userRepository, times(1)).findByUsername(username);
    verify(userRepository, times(1)).existsByUsername(newUsername);
  }

  @Test
  public void updateEmail_whenUserExistsAndNewEmailIsFree_thenSaveInCache() {
    // Prepare
    final var username = "user";
    final var newEmail = "user@mail.ru";
    var mockValueOperations = mock(ValueOperations.class);

    when(userRepository.existsByUsername(username)).thenReturn(true);
    when(userRepository.existsByEmail(newEmail)).thenReturn(false);
    when(redisTemplate.opsForValue()).thenReturn(mockValueOperations);
    doNothing()
        .when(mockValueOperations)
        .set(
            any(String.class),
            eq(ProfileServiceImpl.UpdateEmailCache.class),
            eq(UPDATE_EMAIL_CACHE_TTL_S),
            eq(TimeUnit.SECONDS));

    // Do
    profileService.updateEmail(username, newEmail);

    // Check
    verify(userRepository, times(1)).existsByUsername(username);
    verify(userRepository, times(1)).existsByEmail(newEmail);
    verify(redisTemplate, times(1)).opsForValue();
    verify(mockValueOperations, times(1))
        .set(
            any(String.class),
            any(ProfileServiceImpl.UpdateEmailCache.class),
            eq(UPDATE_EMAIL_CACHE_TTL_S),
            eq(TimeUnit.SECONDS));
  }

  @Test
  public void updateEmail_whenUserNotExists_thenThrownUserNotFoundException() {
    // Prepare
    final var username = "user";
    final var newEmail = "user@mail.ru";

    when(userRepository.existsByUsername(username)).thenReturn(false);

    // Do
    assertThatExceptionOfType(UserNotFoundException.class)
        .isThrownBy(() -> profileService.updateEmail(username, newEmail));

    // Check
    verify(userRepository, times(1)).existsByUsername(username);
  }

  @Test
  public void
      updateEmail_whenUserExistsAndEmailAlreadyExists_thenThrownEmailAlreadyExistsException() {
    // Prepare
    final var username = "user";
    final var newEmail = "user@mail.ru";

    when(userRepository.existsByUsername(username)).thenReturn(true);
    when(userRepository.existsByEmail(newEmail)).thenReturn(true);

    // Do
    assertThatExceptionOfType(EmailAlreadyExistsException.class)
        .isThrownBy(() -> profileService.updateEmail(username, newEmail));

    // Check
    verify(userRepository, times(1)).existsByUsername(username);
    verify(userRepository, times(1)).existsByEmail(newEmail);
  }

  @Test
  public void updateEmailConfirm_whenTokenIsValidAndUserExistsAndNewEmailIsFree_thenUpdateEmail() {
    // Prepare
    final var username = "user";
    final var newEmail = "user@mail.ru";
    final var token = "token";
    var updateEmailCache =
        ProfileServiceImpl.UpdateEmailCache.builder().username(username).newEmail(newEmail).build();
    var user = new User();
    var mockValueOperations = mock(ValueOperations.class);

    when(redisTemplate.opsForValue()).thenReturn(mockValueOperations);
    when(mockValueOperations.get(endsWith(token))).thenReturn(updateEmailCache);
    when(mockValueOperations.getAndDelete(endsWith(token))).thenReturn(updateEmailCache);
    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
    when(userRepository.existsByEmail(newEmail)).thenReturn(false);
    when(userRepository.save(user)).thenReturn(user);

    // Do
    profileService.updateEmailConfirm(username, token);

    // Check
    verify(redisTemplate, times(2)).opsForValue();
    verify(mockValueOperations, times(1)).get(endsWith(token));
    verify(mockValueOperations, times(1)).getAndDelete(endsWith(token));
    verify(userRepository, times(1)).findByUsername(username);
    verify(userRepository, times(1)).existsByEmail(newEmail);
    verify(userRepository, times(1)).save(user);
  }

  @Test
  public void
      updateEmailConfirm_whenTokenHasExpired_thenInvalidOrExpiredConfirmationTokenException() {
    // Prepare
    final var username = "user";
    final var token = "token";
    var mockValueOperations = mock(ValueOperations.class);

    when(redisTemplate.opsForValue()).thenReturn(mockValueOperations);
    when(mockValueOperations.get(endsWith(token))).thenReturn(null);

    // Do
    assertThatExceptionOfType(InvalidOrExpiredConfirmationTokenException.class)
        .isThrownBy(() -> profileService.updateEmailConfirm(username, token));

    // Check
    verify(redisTemplate, times(1)).opsForValue();
    verify(mockValueOperations, times(1)).get(endsWith(token));
  }

  @Test
  public void
      updateEmailConfirm_whenTokenIsInvalid_thenInvalidOrExpiredConfirmationTokenException() {
    // Prepare
    final var username = "user";
    final var otherUsername = "other_user";
    final var token = "token";
    var updateEmailCache =
        ProfileServiceImpl.UpdateEmailCache.builder().username(otherUsername).build();
    var mockValueOperations = mock(ValueOperations.class);

    when(redisTemplate.opsForValue()).thenReturn(mockValueOperations);
    when(mockValueOperations.get(endsWith(token))).thenReturn(updateEmailCache);

    // Do
    assertThatExceptionOfType(InvalidOrExpiredConfirmationTokenException.class)
        .isThrownBy(() -> profileService.updateEmailConfirm(username, token));

    // Check
    verify(redisTemplate, times(1)).opsForValue();
    verify(mockValueOperations, times(1)).get(endsWith(token));
  }

  @Test
  public void updateEmailConfirm_whenTokenIsValidAndUserNotExists_thenUserNotFoundException() {
    // Prepare
    final var username = "user";
    final var token = "token";
    var updateEmailCache = ProfileServiceImpl.UpdateEmailCache.builder().username(username).build();
    var mockValueOperations = mock(ValueOperations.class);

    when(redisTemplate.opsForValue()).thenReturn(mockValueOperations);
    when(mockValueOperations.get(endsWith(token))).thenReturn(updateEmailCache);
    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    // Do
    assertThatExceptionOfType(UserNotFoundException.class)
        .isThrownBy(() -> profileService.updateEmailConfirm(username, token));

    // Check
    verify(redisTemplate, times(2)).opsForValue();
    verify(mockValueOperations, times(1)).get(endsWith(token));
    verify(userRepository, times(1)).findByUsername(username);
  }

  @Test
  public void
      updateEmailConfirm_whenTokenIsValidAndUserExistsAndEmailAlreadyExists_thenEmailAlreadyExistsException() {
    // Prepare
    final var username = "user";
    final var newEmail = "user@mail.ru";
    final var token = "token";
    var updateEmailCache =
        ProfileServiceImpl.UpdateEmailCache.builder().username(username).newEmail(newEmail).build();
    var user = new User();
    var mockValueOperations = mock(ValueOperations.class);

    when(redisTemplate.opsForValue()).thenReturn(mockValueOperations);
    when(mockValueOperations.get(endsWith(token))).thenReturn(updateEmailCache);
    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
    when(userRepository.existsByEmail(updateEmailCache.getNewEmail())).thenReturn(true);

    // Do
    assertThatExceptionOfType(EmailAlreadyExistsException.class)
        .isThrownBy(() -> profileService.updateEmailConfirm(username, token));

    // Check
    verify(redisTemplate, times(2)).opsForValue();
    verify(mockValueOperations, times(1)).get(endsWith(token));
    verify(userRepository, times(1)).findByUsername(username);
    verify(userRepository, times(1)).existsByEmail(updateEmailCache.getNewEmail());
  }

  @Test
  public void
      updatePassword_whenUserExistsAndOldPasswordMatchAndNewPasswordMismatch_thenUpdatePassword() {
    // Prepare
    final var username = "user";
    final var oldPassword = "OldPassword-10";
    final var newPassword = "NewPassword-10";
    var user = new User();
    user.setPassword(oldPassword);

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);
    when(passwordEncoder.matches(newPassword, user.getPassword())).thenReturn(false);
    when(passwordEncoder.encode(newPassword)).thenReturn(newPassword);
    when(userRepository.save(user)).thenReturn(user);

    // Do
    profileService.updatePassword(username, oldPassword, newPassword);

    // Check
    verify(userRepository, times(1)).findByUsername(username);
    verify(passwordEncoder, times(1)).matches(oldPassword, oldPassword);
    verify(passwordEncoder, times(1)).matches(newPassword, oldPassword);
    verify(passwordEncoder, times(1)).encode(newPassword);
    verify(userRepository, times(1)).save(user);
  }

  @Test
  public void updatePassword_whenUserExistsAndOldPasswordMatchAndNewPasswordMatch_thenNothing() {
    // Prepare
    final var username = "user";
    final var oldPassword = "OldPassword-10";
    final var newPassword = "NewPassword-10";
    var user = new User();
    user.setPassword(oldPassword);

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);
    when(passwordEncoder.matches(newPassword, user.getPassword())).thenReturn(true);

    // Do
    profileService.updatePassword(username, oldPassword, newPassword);

    // Check
    verify(userRepository, times(1)).findByUsername(username);
    verify(passwordEncoder, times(1)).matches(oldPassword, oldPassword);
    verify(passwordEncoder, times(1)).matches(newPassword, oldPassword);
  }

  @Test
  public void updatePassword_whenUserNotExists_thenThrownUserNotFoundException() {
    // Prepare
    final var username = "user";
    final var oldPassword = "OldPassword-10";
    final var newPassword = "NewPassword-10";

    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    // Do
    assertThatExceptionOfType(UserNotFoundException.class)
        .isThrownBy(() -> profileService.updatePassword(username, oldPassword, newPassword));

    // Check
    verify(userRepository, times(1)).findByUsername(username);
  }

  @Test
  public void
      updatePassword_whenUserExistsAndOldPasswordMismatch_thenThrownBadOldPasswordException() {
    // Prepare
    final var username = "user";
    final var oldPassword = "OldPassword-10";
    final var newPassword = "NewPassword-10";
    var user = new User();
    user.setPassword(oldPassword);

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(false);

    // Do
    assertThatExceptionOfType(BadOldPasswordException.class)
        .isThrownBy(() -> profileService.updatePassword(username, oldPassword, newPassword));

    // Check
    verify(userRepository, times(1)).findByUsername(username);
    verify(passwordEncoder, times(1)).matches(oldPassword, oldPassword);
  }

  @Test
  public void deleteAccount_whenUserExists_thenDeleteUser() {
    // Prepare
    final var username = "user";
    var user = new User();

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
    doNothing().when(userRepository).delete(user);

    // Do
    profileService.deleteAccount(username);

    // Check
    verify(userRepository, times(1)).findByUsername(username);
    verify(userRepository, times(1)).delete(user);
  }

  @Test
  public void deleteAccount_whenUserNotExists_thenThrownUserNotFoundException() {
    // Prepare
    final var username = "user";

    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    // Do
    assertThatExceptionOfType(UserNotFoundException.class).isThrownBy(() -> profileService.deleteAccount(username));

    // Check
    verify(userRepository, times(1)).findByUsername(username);
  }
}
