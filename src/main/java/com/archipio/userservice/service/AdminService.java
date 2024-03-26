package com.archipio.userservice.service;

public interface AdminService {

  void banAccount(String username);

  void unbanAccount(String username);
}
