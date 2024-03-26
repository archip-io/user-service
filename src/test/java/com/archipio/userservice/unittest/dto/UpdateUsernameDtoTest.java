package com.archipio.userservice.unittest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.archipio.userservice.dto.UpdateUsernameDto;
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

public class UpdateUsernameDtoTest {

  private Validator validator;

  private static Stream<Arguments> provideInvalidUpdateUsernameDto() {
    return Stream.of(
        Arguments.of(UpdateUsernameDto.builder().username(null).build(), Set.of("username")),
        Arguments.of(UpdateUsernameDto.builder().username("").build(), Set.of("username")),
        Arguments.of(UpdateUsernameDto.builder().username("1").build(), Set.of("username")),
        Arguments.of(
            UpdateUsernameDto.builder().username("useruseruseruseruseruseruseruser").build(),
            Set.of("username")),
        Arguments.of(
            UpdateUsernameDto.builder().username("user user").build(), Set.of("username")));
  }

  @BeforeEach
  public void setUp() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  @ParameterizedTest
  @MethodSource("provideInvalidUpdateUsernameDto")
  public void validate_whenUpdateUsernameDtoIsInvalid_thenViolationsIsNotEmpty(
      UpdateUsernameDto updateUsernameDto, Set<String> expectedErrorFields) {
    // Do
    var violations = validator.validate(updateUsernameDto);
    var actualErrorFields =
        violations.stream()
            .map(constraintViolation -> constraintViolation.getPropertyPath().toString())
            .collect(Collectors.toSet());

    // Check
    assertThat(violations.isEmpty()).isFalse();
    assertThat(actualErrorFields).containsExactlyInAnyOrderElementsOf(expectedErrorFields);
  }
}
