package com.archipio.userservice.service;

import com.archipio.userservice.dto.CredentialsInputDto;
import com.archipio.userservice.dto.CredentialsOutputDto;
import com.archipio.userservice.dto.ResetPasswordDto;

public interface UserService {

  void saveCredentials(CredentialsInputDto credentialsInputDto);

  CredentialsOutputDto findByLogin(String login);

  CredentialsOutputDto findByUsernameAndEmail(String username, String email);

  void resetPassword(ResetPasswordDto resetPasswordDto);
}
