package com.archipio.userservice.service.impl;

import static com.archipio.userservice.util.CacheUtils.UPDATE_EMAIL_CACHE_TTL_S;

import com.archipio.userservice.dto.ProfileDto;
import com.archipio.userservice.exception.EmailAlreadyExistsException;
import com.archipio.userservice.exception.UserNotFoundException;
import com.archipio.userservice.exception.UsernameAlreadyExistsException;
import com.archipio.userservice.mapper.UserMapper;
import com.archipio.userservice.persistence.repository.UserRepository;
import com.archipio.userservice.service.ProfileService;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

  private static final String UPDATE_EMAIL_KEY_PREFIX = "service:profile:update_email:";

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final RedisTemplate<String, UpdateEmailCache> redisTemplate;

  @Override
  public ProfileDto getProfileByUsername(String username) {
    var user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    return userMapper.toProfile(user);
  }

  @Override
  public void updateUsername(String username, String newUsername) {
    var user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    if (userRepository.existsByUsername(newUsername)) {
      throw new UsernameAlreadyExistsException();
    }

    user.setUsername(newUsername);
    userRepository.save(user);

    // TODO: Добавить событие о смене имени пользователя
  }

  @Override
  public void updateEmail(String username, String newEmail) {
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

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class UpdateEmailCache {

    private String username;
    private String newEmail;
  }
}
