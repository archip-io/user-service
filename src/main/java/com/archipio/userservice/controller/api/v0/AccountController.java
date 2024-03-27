package com.archipio.userservice.controller.api.v0;

import static com.archipio.userservice.util.ApiUtils.API_V0_PREFIX;
import static com.archipio.userservice.util.ApiUtils.DELETE_ACCOUNT_SUFFIX;
import static com.archipio.userservice.util.ApiUtils.FIND_PROFILE_SUFFIX;
import static com.archipio.userservice.util.ApiUtils.UPDATE_EMAIL_CONFIRM_SUFFIX;
import static com.archipio.userservice.util.ApiUtils.UPDATE_EMAIL_SUFFIX;
import static com.archipio.userservice.util.ApiUtils.UPDATE_PASSWORD_SUFFIX;
import static com.archipio.userservice.util.ApiUtils.UPDATE_USERNAME_SUFFIX;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import com.archipio.commonauth.UserDetailsImpl;
import com.archipio.userservice.dto.ProfileDto;
import com.archipio.userservice.dto.UpdateEmailDto;
import com.archipio.userservice.dto.UpdatePasswordDto;
import com.archipio.userservice.dto.UpdateUsernameDto;
import com.archipio.userservice.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V0_PREFIX)
public class AccountController {

  private final AccountService accountService;

  @GetMapping(FIND_PROFILE_SUFFIX + "/{username}")
  public ResponseEntity<ProfileDto> findProfile(@PathVariable("username") String username) {
    return ResponseEntity.status(OK).body(accountService.getProfileByUsername(username));
  }

  @PreAuthorize("hasAuthority('UPDATE_USERNAME')")
  @PatchMapping(UPDATE_USERNAME_SUFFIX)
  public ResponseEntity<Void> updateUsername(
      @Valid @RequestBody UpdateUsernameDto updateUsernameDto,
      @AuthenticationPrincipal UserDetailsImpl principal) {
    if (!principal.getUsername().equals(updateUsernameDto.getUsername())) {
      accountService.updateUsername(principal.getUsername(), updateUsernameDto.getUsername());
    }
    return ResponseEntity.status(OK).build();
  }

  @PreAuthorize("hasAuthority('UPDATE_EMAIL')")
  @PatchMapping(UPDATE_EMAIL_SUFFIX)
  public ResponseEntity<Void> updateEmail(
      @Valid @RequestBody UpdateEmailDto updateEmailDto,
      @AuthenticationPrincipal UserDetailsImpl principal) {
    if (!principal.getEmail().equals(updateEmailDto.getEmail())) {
      accountService.updateEmail(principal.getUsername(), updateEmailDto.getEmail());
    }
    return ResponseEntity.status(ACCEPTED).build();
  }

  @PreAuthorize("hasAuthority('UPDATE_EMAIL')")
  @GetMapping(UPDATE_EMAIL_CONFIRM_SUFFIX)
  public ResponseEntity<Void> updateEmailConfirm(
      @RequestParam("token") String token, @AuthenticationPrincipal UserDetailsImpl principal) {
    accountService.updateEmailConfirm(principal.getUsername(), token);
    return ResponseEntity.status(OK).build();
  }

  @PreAuthorize("hasAuthority('UPDATE_PASSWORD')")
  @PatchMapping(UPDATE_PASSWORD_SUFFIX)
  public ResponseEntity<Void> updatePassword(
      @Valid @RequestBody UpdatePasswordDto updatePasswordDto,
      @AuthenticationPrincipal UserDetailsImpl principal) {
    accountService.updatePassword(
        principal.getUsername(),
        updatePasswordDto.getOldPassword(),
        updatePasswordDto.getNewPassword());
    return ResponseEntity.status(OK).build();
  }

  @PreAuthorize("hasAuthority('DELETE_ACCOUNT')")
  @DeleteMapping(DELETE_ACCOUNT_SUFFIX)
  public ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal UserDetailsImpl principal) {
    accountService.deleteAccount(principal.getUsername());
    return ResponseEntity.status(NO_CONTENT).build();
  }
}
