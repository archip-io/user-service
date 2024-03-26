package com.archipio.userservice.service;

import com.archipio.userservice.dto.ProfileDto;

public interface ProfileService {

  ProfileDto getProfileByUsername(String username);

  void updateUsername(String username, String newUsername);

  void updateEmail(String username, String newEmail);

  void updateEmailConfirm(String username, String token);

  void updatePassword(String username, String oldPassword, String newPassword);
}
