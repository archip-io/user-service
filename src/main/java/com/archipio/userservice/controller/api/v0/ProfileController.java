package com.archipio.userservice.controller.api.v0;

import static com.archipio.userservice.util.ApiUtils.API_V0_PREFIX;
import static com.archipio.userservice.util.ApiUtils.FIND_PROFILE_SUFFIX;
import static com.archipio.userservice.util.ApiUtils.UPDATE_USERNAME_SUFFIX;
import static org.springframework.http.HttpStatus.OK;

import com.archipio.commonauth.UserDetailsImpl;
import com.archipio.userservice.dto.ProfileDto;
import com.archipio.userservice.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V0_PREFIX)
public class ProfileController {

  private final ProfileService profileService;

  @GetMapping(FIND_PROFILE_SUFFIX + "{username}")
  public ResponseEntity<ProfileDto> findProfile(@PathVariable("username") String username) {
    return ResponseEntity.status(OK).body(profileService.getProfileByUsername(username));
  }

  @PreAuthorize("hasAuthority('UPDATE_USERNAME')")
  @PutMapping(UPDATE_USERNAME_SUFFIX)
  public ResponseEntity<Void> updateUsername(
      @RequestParam("username") String username,
      @AuthenticationPrincipal UserDetailsImpl principal) {
    if (!principal.getUsername().equals(username)) {
      profileService.updateUsername(principal.getUsername(), username);
    }
    return ResponseEntity.status(OK).build();
  }
}
