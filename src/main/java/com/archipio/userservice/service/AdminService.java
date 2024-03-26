package com.archipio.userservice.service;

import lombok.NonNull;

public interface AdminService {

  void banAccount(@NonNull String username);

  void unbanAccount(@NonNull String username);

  void deleteUserAccount(@NonNull String username);
}
