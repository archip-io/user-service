package com.archipio.userservice.service;

import com.archipio.userservice.dto.CredentialsInputDto;
import com.archipio.userservice.dto.CredentialsOutputDto;
import com.archipio.userservice.dto.ResetPasswordDto;
import com.archipio.userservice.dto.ValidatePasswordDto;
import lombok.NonNull;

public interface UserService {

  void saveCredentials(CredentialsInputDto credentialsInputDto);

  CredentialsOutputDto findByLogin(@NonNull String login);

  CredentialsOutputDto findByUsernameAndEmail(@NonNull String username, @NonNull String email);

  void resetPassword(ResetPasswordDto resetPasswordDto);

  void validatePassword(ValidatePasswordDto validatePasswordDto);
}
