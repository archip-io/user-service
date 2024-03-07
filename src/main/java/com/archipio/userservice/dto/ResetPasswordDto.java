package com.archipio.userservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Map;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import static com.archipio.userservice.util.ValidationUtils.MAX_PASSWORD_LENGTH;
import static com.archipio.userservice.util.ValidationUtils.MIN_PASSWORD_LENGTH;
import static com.archipio.userservice.util.ValidationUtils.PASSWORD_REGEX;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPasswordDto {

  @NotNull(message = "{validation.login.not-null}")
  @Schema(description = "Логин", requiredMode = Schema.RequiredMode.REQUIRED)
  private String login;

  @NotNull(message = "{validation.password.not-null}")
  @Length(
          min = MIN_PASSWORD_LENGTH,
          max = MAX_PASSWORD_LENGTH,
          message = "{validation.password.length}")
  @Pattern(regexp = PASSWORD_REGEX, message = "{validation.password.pattern}")
  @Schema(description = "Сообщение ошибки", requiredMode = Schema.RequiredMode.REQUIRED)
  private String password;
}
