package com.archipio.userservice.unittest.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.quality.Strictness.LENIENT;

import com.archipio.userservice.dto.CredentialsInputDto;
import com.archipio.userservice.dto.CredentialsOutputDto;
import com.archipio.userservice.dto.ResetPasswordDto;
import com.archipio.userservice.dto.ValidatePasswordDto;
import com.archipio.userservice.exception.BadPasswordException;
import com.archipio.userservice.exception.EmailAlreadyExistsException;
import com.archipio.userservice.exception.RoleNotFoundException;
import com.archipio.userservice.exception.UserNotFoundException;
import com.archipio.userservice.exception.UsernameAlreadyExistsException;
import com.archipio.userservice.mapper.UserMapper;
import com.archipio.userservice.persistence.entity.Role;
import com.archipio.userservice.persistence.entity.User;
import com.archipio.userservice.persistence.repository.RoleRepository;
import com.archipio.userservice.persistence.repository.UserRepository;
import com.archipio.userservice.service.impl.UserServiceImpl;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = LENIENT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceImplTest {

  @Mock private UserRepository userRepository;
  @Mock private RoleRepository roleRepository;
  @Mock private UserMapper userMapper;
  @Mock private BCryptPasswordEncoder passwordEncoder;
  @InjectMocks private UserServiceImpl userService;

  private static Stream<Arguments> provideNullParameters_findByLogin() {
    return Stream.of(Arguments.of((String) null));
  }

  private static Stream<Arguments> provideNullParameters_findByUsernameAndEmail() {
    return Stream.of(Arguments.of(null, "user@mail.ru"), Arguments.of("username", null));
  }

  @Test
  @Order(2)
  public void saveCredentials_whenUniqueUser_thenSaveUser() {
    // Prepare
    final var username = "user";
    final var email = "email";
    final var password = "password";
    final var roleId = 0;
    final var roleName = "USER";
    final var userDto =
        CredentialsInputDto.builder().username(username).email(email).password(password).build();

    final var user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(password);

    final var role = new Role();
    role.setId(roleId);
    role.setName(roleName);

    when(userRepository.existsByUsername(username)).thenReturn(false);
    when(userRepository.existsByEmail(email)).thenReturn(false);
    when(passwordEncoder.encode(password)).thenReturn(password);
    when(userMapper.toEntity(userDto)).thenReturn(user);
    when(roleRepository.findByName(roleName)).thenReturn(Optional.of(role));
    when(userRepository.save(user)).thenReturn(user);

    // Do
    userService.saveCredentials(userDto);

    // Check
    verify(userRepository, times(1)).existsByUsername(username);
    verify(userRepository, times(1)).existsByEmail(email);
    verify(passwordEncoder, times(1)).encode(password);
    verify(userMapper, times(1)).toEntity(userDto);
    verify(roleRepository, times(1)).findByName(roleName);
    verify(userRepository, times(1)).save(user);

    assertThat(user.getRole()).isEqualTo(role);

    // Do (проверка ленивой загрузки роли)
    userService.saveCredentials(userDto);

    // Check
    verify(userRepository, times(2)).existsByUsername(username);
    verify(userRepository, times(2)).existsByEmail(email);
    verify(passwordEncoder, times(2)).encode(password);
    verify(userMapper, times(2)).toEntity(userDto);
    verify(roleRepository, times(1)).findByName(roleName);
    verify(userRepository, times(2)).save(user);

    assertThat(user.getRole()).isEqualTo(role);
  }

  @Test
  public void saveCredentials_whenUsernameAlreadyExists_thenThrownUsernameAlreadyExistsException() {
    // Prepare
    final var username = "user";
    final var email = "email";
    final var password = "password";
    final var userDto =
        CredentialsInputDto.builder().username(username).email(email).password(password).build();
    when(userRepository.existsByUsername(username)).thenReturn(true);

    // Do
    assertThatExceptionOfType(UsernameAlreadyExistsException.class)
        .isThrownBy(() -> userService.saveCredentials(userDto));

    // Check
    verify(userRepository, times(1)).existsByUsername(username);
  }

  @Test
  public void saveCredentials_whenEmailAlreadyExists_thenThrownEmailAlreadyExistsException() {
    // Prepare
    final var username = "user";
    final var email = "email";
    final var password = "password";
    final var userDto =
        CredentialsInputDto.builder().username(username).email(email).password(password).build();
    when(userRepository.existsByUsername(username)).thenReturn(false);
    when(userRepository.existsByEmail(email)).thenReturn(true);

    // Do
    assertThatExceptionOfType(EmailAlreadyExistsException.class)
        .isThrownBy(() -> userService.saveCredentials(userDto));

    // Check
    verify(userRepository, times(1)).existsByUsername(username);
    verify(userRepository, times(1)).existsByEmail(email);
  }

  @Test
  @Order(1)
  public void saveCredentials_whenNotFoundRole_thenThrownRoleNotFoundException() {
    // Prepare
    final var username = "user";
    final var email = "email";
    final var password = "password";
    final var roleName = "USER";
    final var userDto =
        CredentialsInputDto.builder().username(username).email(email).password(password).build();

    final var user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(password);

    when(userRepository.existsByUsername(username)).thenReturn(false);
    when(userRepository.existsByEmail(email)).thenReturn(false);
    when(passwordEncoder.encode(password)).thenReturn(password);
    when(userMapper.toEntity(userDto)).thenReturn(user);
    when(roleRepository.findByName(roleName)).thenReturn(Optional.empty());

    // Do
    assertThatExceptionOfType(RoleNotFoundException.class)
        .isThrownBy(() -> userService.saveCredentials(userDto));

    // Check
    verify(userRepository, times(1)).existsByUsername(username);
    verify(userRepository, times(1)).existsByEmail(email);
    verify(passwordEncoder, times(1)).encode(password);
    verify(userMapper, times(1)).toEntity(userDto);
    verify(roleRepository, times(1)).findByName(roleName);
  }

  @Test
  public void findByLogin_whenUserExists_thenReturnCredentials() {
    // Prepare
    final var login = "login";
    final var user = new User();
    final var credentialsOutputDto = CredentialsOutputDto.builder().build();
    when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));
    when(userMapper.toCredentialsOutput(user)).thenReturn(credentialsOutputDto);

    // Do
    var actualCredentialsOutputDto = userService.findByLogin(login);

    // Check
    verify(userRepository, times(1)).findByLogin(login);
    verify(userMapper, times(1)).toCredentialsOutput(user);

    assertThat(actualCredentialsOutputDto).isEqualTo(credentialsOutputDto);
  }

  @ParameterizedTest
  @MethodSource("provideNullParameters_findByLogin")
  public void findByLogin_whenParametersIsNull_thenNullPointerException(String username) {
    assertThatNullPointerException().isThrownBy(() -> userService.findByLogin(username));
  }

  @Test
  public void findByLogin_whenUserNotFound_thenThrownUserNotFoundException() {
    // Prepare
    final var login = "login";
    when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

    // Do
    assertThatExceptionOfType(UserNotFoundException.class)
        .isThrownBy(() -> userService.findByLogin(login));

    // Check
    verify(userRepository, times(1)).findByLogin(login);
  }

  @Test
  public void findByUsernameAndEmail_whenUserExists_thenReturnCredentials() {
    // Prepare
    final var username = "user";
    final var email = "email";
    final var user = new User();
    final var credentialsOutputDto = CredentialsOutputDto.builder().build();
    when(userRepository.findByUsernameAndEmail(username, email)).thenReturn(Optional.of(user));
    when(userMapper.toCredentialsOutput(user)).thenReturn(credentialsOutputDto);

    // Do
    var actualCredentialsOutputDto = userService.findByUsernameAndEmail(username, email);

    // Check
    verify(userRepository, times(1)).findByUsernameAndEmail(username, email);
    verify(userMapper, times(1)).toCredentialsOutput(user);

    assertThat(actualCredentialsOutputDto).isEqualTo(credentialsOutputDto);
  }

  @ParameterizedTest
  @MethodSource("provideNullParameters_findByUsernameAndEmail")
  public void findByUsernameAndEmail_whenParametersIsNull_thenNullPointerException(
      String username, String email) {
    assertThatNullPointerException()
        .isThrownBy(() -> userService.findByUsernameAndEmail(username, email));
  }

  @Test
  public void findByUsernameAndEmail_whenUserNotFound_thenThrownUserNotFoundException() {
    // Prepare
    final var username = "user";
    final var email = "email";
    when(userRepository.findByUsernameAndEmail(username, email)).thenReturn(Optional.empty());

    // Do
    assertThatExceptionOfType(UserNotFoundException.class)
        .isThrownBy(() -> userService.findByUsernameAndEmail(username, email));

    // Check
    verify(userRepository, times(1)).findByUsernameAndEmail(username, email);
  }

  @Test
  public void resetPassword_whenUserExists_thenSavePasswordInCache() {
    // Prepare
    final var login = "login";
    final var password = "password";
    final var user = new User();
    final var resetPasswordDto = ResetPasswordDto.builder().login(login).password(password).build();
    when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));
    when(passwordEncoder.encode(password)).thenReturn(password);
    when(userRepository.save(user)).thenReturn(user);

    // Do
    userService.resetPassword(resetPasswordDto);

    // Check
    verify(userRepository, times(1)).findByLogin(login);
    verify(passwordEncoder, times(1)).encode(password);
    verify(userRepository, times(1)).save(user);
  }

  @Test
  public void resetPassword_whenUserNotFound_thenThrownUserNotFoundException() {
    // Prepare
    final var login = "login";
    final var password = "password";
    final var resetPasswordDto = ResetPasswordDto.builder().login(login).password(password).build();
    when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

    // Do
    assertThatExceptionOfType(UserNotFoundException.class)
        .isThrownBy(() -> userService.resetPassword(resetPasswordDto));

    // Check
    verify(userRepository, times(1)).findByLogin(login);
  }

  @Test
  public void validatePassword_whenUserExistsAndPasswordIsCorrect_thenNothing() {
    // Prepare
    final var login = "login";
    final var password = "password";

    final var user = new User();
    user.setPassword(password);

    final var validatePasswordDto =
        ValidatePasswordDto.builder().login(login).password(password).build();
    when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

    // Do
    userService.validatePassword(validatePasswordDto);

    // Check
    verify(userRepository, times(1)).findByLogin(login);
    verify(passwordEncoder, times(1)).matches(password, user.getPassword());
  }

  @Test
  public void validatePassword_whenUserNotFound_thenThrownUserNotFoundException() {
    // Prepare
    final var login = "login";
    final var password = "password";
    final var validatePasswordDto =
        ValidatePasswordDto.builder().login(login).password(password).build();
    when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

    // Do
    assertThatExceptionOfType(UserNotFoundException.class)
        .isThrownBy(() -> userService.validatePassword(validatePasswordDto));

    // Check
    verify(userRepository, times(1)).findByLogin(login);
  }

  @Test
  public void
      validatePassword_whenUserExistsAndPasswordIsIncorrect_thenThrownBadPasswordException() {
    // Prepare
    final var login = "login";
    final var password = "password";

    final var user = new User();
    user.setPassword(password);

    final var validatePasswordDto =
        ValidatePasswordDto.builder().login(login).password(password).build();
    when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

    // Do
    assertThatExceptionOfType(BadPasswordException.class)
        .isThrownBy(() -> userService.validatePassword(validatePasswordDto));

    // Check
    verify(userRepository, times(1)).findByLogin(login);
    verify(passwordEncoder, times(1)).matches(password, user.getPassword());
  }
}
