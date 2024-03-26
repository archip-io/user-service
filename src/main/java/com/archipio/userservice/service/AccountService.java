package com.archipio.userservice.service;

import com.archipio.userservice.dto.ProfileDto;
import lombok.NonNull;

public interface AccountService {

  ProfileDto getProfileByUsername(@NonNull String username);

  void updateUsername(@NonNull String username, @NonNull String newUsername);

  void updateEmail(@NonNull String username, @NonNull String newEmail);

  void updateEmailConfirm(@NonNull String username, @NonNull String token);

  void updatePassword(
      @NonNull String username, @NonNull String oldPassword, @NonNull String newPassword);

  void deleteAccount(@NonNull String username);
}
