package com.archipio.userservice.unittest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.archipio.userservice.dto.ValidatePasswordDto;
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

public class ValidatePasswordDtoTest {

  private Validator validator;

  private static Stream<Arguments> provideInvalidValidatePasswordDto() {
    return Stream.of(
        Arguments.of(
            ValidatePasswordDto.builder().login(null).password("Password_10").build(),
            Set.of("login")),
        Arguments.of(
            ValidatePasswordDto.builder().login("login").password(null).build(),
            Set.of("password")));
  }

  @BeforeEach
  public void setUp() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  @ParameterizedTest
  @MethodSource("provideInvalidValidatePasswordDto")
  public void validate_invalidValidatePasswordDto_violationsIsNotEmpty(
      ValidatePasswordDto validatePasswordDto, Set<String> expectedErrorFields) {
    // Do
    var violations = validator.validate(validatePasswordDto);
    var actualErrorFields =
        violations.stream()
            .map(constraintViolation -> constraintViolation.getPropertyPath().toString())
            .collect(Collectors.toSet());

    // Check
    assertThat(violations.isEmpty()).isFalse();
    assertThat(actualErrorFields).containsExactlyInAnyOrderElementsOf(expectedErrorFields);
  }
}
