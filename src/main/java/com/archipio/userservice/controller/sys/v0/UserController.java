package com.archipio.userservice.controller.sys.v0;

import static com.archipio.userservice.util.ApiUtils.CREATE_USER_SUFFIX;
import static com.archipio.userservice.util.ApiUtils.SYS_V0_PREFIX;
import static org.springframework.http.HttpStatus.CREATED;

import com.archipio.userservice.dto.UserDto;
import com.archipio.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(SYS_V0_PREFIX)
public class UserController {

  private final UserService userService;

  @PostMapping(CREATE_USER_SUFFIX)
  public ResponseEntity<Void> createUser(@Valid @RequestBody UserDto userDto) {
    userService.createUser(userDto);
    return ResponseEntity.status(CREATED).build();
  }
}
