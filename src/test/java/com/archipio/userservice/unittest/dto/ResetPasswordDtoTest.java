package com.archipio.userservice.unittest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.archipio.userservice.dto.ResetPasswordDto;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ResetPasswordDtoTest {

  private Validator validator;

  private static Stream<Arguments> provideInvalidResetPasswordDto() {
    return Stream.of(
        Arguments.of(
            ResetPasswordDto.builder().login(null).password("Password_10").build(),
            Set.of("login")),
        Arguments.of(
            ResetPasswordDto.builder().login("login").password(null).build(), Set.of("password")),
        Arguments.of(
            ResetPasswordDto.builder().login("login").password("Pw_1").build(), Set.of("password")),
        Arguments.of(
            ResetPasswordDto.builder()
                .login("login")
                .password("Password_10Password_10Password_10Password_10Password_10Password_10")
                .build(),
            Set.of("password")),
        Arguments.of(
            ResetPasswordDto.builder().login("login").password("password_10").build(),
            Set.of("password")),
        Arguments.of(
            ResetPasswordDto.builder().login("login").password("Password10").build(),
            Set.of("password")),
        Arguments.of(
            ResetPasswordDto.builder().login("login").password("Password_").build(),
            Set.of("password")));
  }

  @BeforeEach
  public void setUp() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  @ParameterizedTest
  @MethodSource("provideInvalidResetPasswordDto")
  public void validate_whenResetPasswordDtoIsInvalid_thenViolationsIsNotEmpty(
      ResetPasswordDto resetPasswordDto, Set<String> expectedErrorFields) {
    // Do
    var violations = validator.validate(resetPasswordDto);
    var actualErrorFields =
        violations.stream()
            .map(constraintViolation -> constraintViolation.getPropertyPath().toString())
            .collect(Collectors.toSet());

    // Check
    assertThat(violations.isEmpty()).isFalse();
    assertThat(actualErrorFields).containsExactlyInAnyOrderElementsOf(expectedErrorFields);
  }
}
