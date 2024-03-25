package com.archipio.userservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiUtils {

  public static final String API_V0_PREFIX = "/api/v0/users";
  public static final String FIND_PROFILE_SUFFIX = "";
  public static final String SYS_V0_PREFIX = "/sys/v0/users";
  public static final String SAVE_CREDENTIALS_SUFFIX = "";
  public static final String FIND_CREDENTIALS_SUFFIX = "";
  public static final String RESET_PASSWORD_SUFFIX = "/reset-password";
  public static final String VALIDATE_PASSWORD_SUFFIX = "/validate-password";
}
