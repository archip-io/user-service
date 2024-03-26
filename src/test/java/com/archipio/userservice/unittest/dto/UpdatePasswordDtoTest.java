package com.archipio.userservice.unittest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.archipio.userservice.dto.UpdatePasswordDto;
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

public class UpdatePasswordDtoTest {

  private Validator validator;

  private static Stream<Arguments> provideInvalidUpdatePasswordDto() {
    return Stream.of(
        Arguments.of(UpdatePasswordDto.builder().newPassword(null).build(), Set.of("newPassword")),
        Arguments.of(
            UpdatePasswordDto.builder().newPassword("Pw_1").build(), Set.of("newPassword")),
        Arguments.of(
            UpdatePasswordDto.builder()
                .newPassword("Password_10Password_10Password_10Password_10Password_10Password_10")
                .build(),
            Set.of("newPassword")),
        Arguments.of(
            UpdatePasswordDto.builder().newPassword("password_10").build(), Set.of("newPassword")),
        Arguments.of(
            UpdatePasswordDto.builder().newPassword("Password10").build(), Set.of("newPassword")),
        Arguments.of(
            UpdatePasswordDto.builder().newPassword("Password_").build(), Set.of("newPassword")));
  }

  @BeforeEach
  public void setUp() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  @ParameterizedTest
  @MethodSource("provideInvalidUpdatePasswordDto")
  public void validate_invalidUpdatePasswordDto_violationsIsNotEmpty(
      UpdatePasswordDto updatePasswordDto, Set<String> expectedErrorFields) {
    // Do
    var violations = validator.validate(updatePasswordDto);
    var actualErrorFields =
        violations.stream()
            .map(constraintViolation -> constraintViolation.getPropertyPath().toString())
            .collect(Collectors.toSet());

    // Check
    assertThat(violations.isEmpty()).isFalse();
    assertThat(actualErrorFields).containsExactlyInAnyOrderElementsOf(expectedErrorFields);
  }
}
