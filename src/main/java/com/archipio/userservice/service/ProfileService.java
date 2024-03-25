package com.archipio.userservice.service;

import com.archipio.userservice.dto.ProfileDto;

public interface ProfileService {

  ProfileDto getProfileByUsername(String username);

  void updateUsername(String username, String newUsername);
}
