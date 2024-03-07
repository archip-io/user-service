package com.archipio.userservice.unittest.dto;

import com.archipio.userservice.dto.UserDto;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDtoTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @ParameterizedTest
    @MethodSource("provideInvalidCredentialsInputDto")
    public void validate_invalidCredentialsInputDto_violationsIsNotEmpty(
            UserDto userDto, Set<String> expectedErrorFields) {
        // Do
        var violations = validator.validate(userDto);
        var actualErrorFields =
                violations.stream()
                        .map(constraintViolation -> constraintViolation.getPropertyPath().toString())
                        .collect(Collectors.toSet());

        // Check
        assertThat(violations.isEmpty()).isFalse();
        assertThat(actualErrorFields).containsExactlyInAnyOrderElementsOf(expectedErrorFields);
    }

    private static Stream<Arguments> provideInvalidCredentialsInputDto() {
        return Stream.of(
                Arguments.of(
                        UserDto.builder()
                                .username(null)
                                .email("user@mail.ru")
                                .password("Password_10")
                                .build(),
                        Set.of("username")),
                Arguments.of(
                        UserDto.builder()
                                .username("")
                                .email("user@mail.ru")
                                .password("Password_10")
                                .build(),
                        Set.of("username")),
                Arguments.of(
                        UserDto.builder()
                                .username("1")
                                .email("user@mail.ru")
                                .password("Password_10")
                                .build(),
                        Set.of("username")),
                Arguments.of(
                        UserDto.builder()
                                .username("useruseruseruseruseruseruseruser")
                                .email("user@mail.ru")
                                .password("Password_10")
                                .build(),
                        Set.of("username")),
                Arguments.of(
                        UserDto.builder()
                                .username("user user")
                                .email("user@mail.ru")
                                .password("Password_10")
                                .build(),
                        Set.of("username")),
                Arguments.of(
                        UserDto.builder()
                                .username("username")
                                .email(null)
                                .password("Password_10")
                                .build(),
                        Set.of("email")),
                Arguments.of(
                        UserDto.builder()
                                .username("username")
                                .email("usermail.ru")
                                .password("Password_10")
                                .build(),
                        Set.of("email")),
                Arguments.of(
                        UserDto.builder()
                                .username("username")
                                .email("user@mail.ru")
                                .password(null)
                                .build(),
                        Set.of("password")),
                Arguments.of(
                        UserDto.builder()
                                .username("username")
                                .email("user@mail.ru")
                                .password("Pw_1")
                                .build(),
                        Set.of("password")),
                Arguments.of(
                        UserDto.builder()
                                .username("username")
                                .email("user@mail.ru")
                                .password("Password_10Password_10Password_10Password_10Password_10Password_10")
                                .build(),
                        Set.of("password")),
                Arguments.of(
                        UserDto.builder()
                                .username("username")
                                .email("user@mail.ru")
                                .password("password_10")
                                .build(),
                        Set.of("password")),
                Arguments.of(
                        UserDto.builder()
                                .username("username")
                                .email("user@mail.ru")
                                .password("Password10")
                                .build(),
                        Set.of("password")),
                Arguments.of(
                        UserDto.builder()
                                .username("username")
                                .email("user@mail.ru")
                                .password("Password_")
                                .build(),
                        Set.of("password")));
    }
}
