package com.archipio.userservice.service.impl;

import static com.archipio.userservice.util.CacheUtils.UPDATE_EMAIL_CACHE_TTL_S;

import com.archipio.userservice.dto.ProfileDto;
import com.archipio.userservice.exception.BadOldPasswordException;
import com.archipio.userservice.exception.EmailAlreadyExistsException;
import com.archipio.userservice.exception.InvalidOrExpiredConfirmationTokenException;
import com.archipio.userservice.exception.UserNotFoundException;
import com.archipio.userservice.exception.UsernameAlreadyExistsException;
import com.archipio.userservice.mapper.UserMapper;
import com.archipio.userservice.persistence.repository.UserRepository;
import com.archipio.userservice.service.AccountService;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

  private static final String UPDATE_EMAIL_KEY_PREFIX = "service:profile:update_email:";

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final RedisTemplate<String, UpdateEmailCache> redisTemplate;
  private final PasswordEncoder passwordEncoder;

  @Override
  public ProfileDto getProfileByUsername(@NonNull String username) {
    var user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    return userMapper.toProfile(user);
  }

  @Transactional
  @Override
  public void updateUsername(@NonNull String username, @NonNull String newUsername) {
    var user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    if (userRepository.existsByUsername(newUsername)) {
      throw new UsernameAlreadyExistsException();
    }

    user.setUsername(newUsername);
    userRepository.save(user);

    // TODO: Добавить событие о смене имени пользователя
  }

  @Override
  public void updateEmail(@NonNull String username, @NonNull String newEmail) {
    if (!userRepository.existsByUsername(username)) {
      throw new UserNotFoundException();
    }
    if (userRepository.existsByEmail(newEmail)) {
      throw new EmailAlreadyExistsException();
    }

    var token = UUID.randomUUID().toString();
    var cache = UpdateEmailCache.builder().username(username).newEmail(newEmail).build();
    redisTemplate
        .opsForValue()
        .set(UPDATE_EMAIL_KEY_PREFIX + token, cache, UPDATE_EMAIL_CACHE_TTL_S, TimeUnit.SECONDS);

    // TODO: Добавить событие отправки письма
  }

  @Transactional
  @Override
  public void updateEmailConfirm(@NonNull String username, @NonNull String token) {
    var registrationDto = redisTemplate.opsForValue().get(UPDATE_EMAIL_KEY_PREFIX + token);
    if (registrationDto == null || !registrationDto.getUsername().equals(username)) {
      throw new InvalidOrExpiredConfirmationTokenException();
    }

    redisTemplate.opsForValue().getAndDelete(UPDATE_EMAIL_KEY_PREFIX + token);

    var user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    if (userRepository.existsByEmail(registrationDto.newEmail)) {
      throw new EmailAlreadyExistsException();
    }

    user.setEmail(registrationDto.getNewEmail());
    userRepository.save(user);

    // TODO: Добавить событие о смене email
  }

  @Transactional
  @Override
  public void updatePassword(
      @NonNull String username, @NonNull String oldPassword, @NonNull String newPassword) {
    var user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
      throw new BadOldPasswordException();
    }
    if (passwordEncoder.matches(newPassword, user.getPassword())) {
      return;
    }

    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
  }

  @Transactional
  @Override
  public void deleteAccount(@NonNull String username) {
    var user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    userRepository.delete(user);
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class UpdateEmailCache {

    private String username;
    private String newEmail;
  }
}
