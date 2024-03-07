package com.archipio.userservice.service;

import com.archipio.userservice.dto.CredentialsInputDto;
import com.archipio.userservice.dto.CredentialsOutputDto;

public interface UserService {

  void saveCredentials(CredentialsInputDto credentialsInputDto);

  CredentialsOutputDto findByLogin(String login);
}
