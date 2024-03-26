package com.archipio.userservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiUtils {

  public static final String API_V0_PREFIX = "/api/v0/users";
  public static final String FIND_PROFILE_SUFFIX = "";
  public static final String UPDATE_USERNAME_SUFFIX = "/update-username";
  public static final String UPDATE_EMAIL_SUFFIX = "/update-email";
  public static final String UPDATE_EMAIL_CONFIRM_SUFFIX = "/update-email/confirm";
  public static final String UPDATE_PASSWORD_SUFFIX = "/update-password";
  public static final String DELETE_ACCOUNT_SUFFIX = "";
  public static final String SYS_V0_PREFIX = "/sys/v0/users";
  public static final String SAVE_CREDENTIALS_SUFFIX = "";
  public static final String FIND_CREDENTIALS_SUFFIX = "";
  public static final String RESET_PASSWORD_SUFFIX = "/reset-password";
  public static final String VALIDATE_PASSWORD_SUFFIX = "/validate-password";
}
