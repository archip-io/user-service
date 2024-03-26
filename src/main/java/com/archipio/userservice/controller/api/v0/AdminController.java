package com.archipio.userservice.controller.api.v0;

import static com.archipio.userservice.util.ApiUtils.ADMIN_PREFIX;
import static com.archipio.userservice.util.ApiUtils.API_V0_PREFIX;
import static com.archipio.userservice.util.ApiUtils.BAN_SUFFIX;
import static com.archipio.userservice.util.ApiUtils.UNBAN_SUFFIX;
import static org.springframework.http.HttpStatus.OK;

import com.archipio.userservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(API_V0_PREFIX + ADMIN_PREFIX)
@RequiredArgsConstructor
public class AdminController {

  private final AdminService adminService;

  @PreAuthorize("hasAuthority('BAN_ACCOUNT')")
  @PatchMapping(BAN_SUFFIX)
  public ResponseEntity<Void> banAccount(@RequestParam("username") String username) {
    adminService.banAccount(username);
    return ResponseEntity.status(OK).build();
  }

  @PreAuthorize("hasAuthority('UNBAN_ACCOUNT')")
  @PatchMapping(UNBAN_SUFFIX)
  public ResponseEntity<Void> unbanAccount(@RequestParam("username") String username) {
    adminService.unbanAccount(username);
    return ResponseEntity.status(OK).build();
  }
}
