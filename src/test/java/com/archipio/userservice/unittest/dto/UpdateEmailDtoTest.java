package com.archipio.userservice.unittest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.archipio.userservice.dto.UpdateEmailDto;
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

public class UpdateEmailDtoTest {

  private Validator validator;

  private static Stream<Arguments> provideInvalidUpdateEmailDto() {
    return Stream.of(
        Arguments.of(UpdateEmailDto.builder().email(null).build(), Set.of("email")),
        Arguments.of(UpdateEmailDto.builder().email("usermail.ru").build(), Set.of("email")));
  }

  @BeforeEach
  public void setUp() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  @ParameterizedTest
  @MethodSource("provideInvalidUpdateEmailDto")
  public void validate_invalidUpdateEmailDto_violationsIsNotEmpty(
      UpdateEmailDto updateEmailDto, Set<String> expectedErrorFields) {
    // Do
    var violations = validator.validate(updateEmailDto);
    var actualErrorFields =
        violations.stream()
            .map(constraintViolation -> constraintViolation.getPropertyPath().toString())
            .collect(Collectors.toSet());

    // Check
    assertThat(violations.isEmpty()).isFalse();
    assertThat(actualErrorFields).containsExactlyInAnyOrderElementsOf(expectedErrorFields);
  }
}
