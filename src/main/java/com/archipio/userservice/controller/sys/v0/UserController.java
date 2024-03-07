package com.archipio.userservice.controller.sys.v0;

import static com.archipio.userservice.util.ApiUtils.FIND_CREDENTIALS_SUFFIX;
import static com.archipio.userservice.util.ApiUtils.RESET_PASSWORD_SUFFIX;
import static com.archipio.userservice.util.ApiUtils.SAVE_CREDENTIALS_SUFFIX;
import static com.archipio.userservice.util.ApiUtils.SYS_V0_PREFIX;
import static com.archipio.userservice.util.ApiUtils.VALIDATE_PASSWORD_SUFFIX;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.archipio.userservice.dto.CredentialsInputDto;
import com.archipio.userservice.dto.CredentialsOutputDto;
import com.archipio.userservice.dto.ResetPasswordDto;
import com.archipio.userservice.dto.ValidatePasswordDto;
import com.archipio.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(SYS_V0_PREFIX)
public class UserController {

  private final UserService userService;

  @PostMapping(SAVE_CREDENTIALS_SUFFIX)
  public ResponseEntity<Void> saveCredentials(
      @Valid @RequestBody CredentialsInputDto credentialsInputDto) {
    userService.saveCredentials(credentialsInputDto);
    return ResponseEntity.status(CREATED).build();
  }

  @GetMapping(FIND_CREDENTIALS_SUFFIX)
  public ResponseEntity<CredentialsOutputDto> findCredentialsByLogin(
      @RequestParam(value = "login", required = false) String login,
      @RequestParam(value = "username", required = false) String username,
      @RequestParam(value = "email", required = false) String email)
      throws MissingServletRequestParameterException {
    if (login != null) {
      return ResponseEntity.status(OK).body(userService.findByLogin(login));
    }
    if (username != null && email != null) {
      return ResponseEntity.status(OK).body(userService.findByUsernameAndEmail(username, email));
    }
    throw new MissingServletRequestParameterException("login", String.class.getTypeName());
  }

  @PutMapping(RESET_PASSWORD_SUFFIX)
  public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
    userService.resetPassword(resetPasswordDto);
    return ResponseEntity.status(OK).build();
  }

  @PostMapping(VALIDATE_PASSWORD_SUFFIX)
  public ResponseEntity<Void> validatePassword(@Valid @RequestBody ValidatePasswordDto validatePasswordDto) {
    userService.validatePassword(validatePasswordDto);
    return ResponseEntity.status(OK).build();
  }
}
