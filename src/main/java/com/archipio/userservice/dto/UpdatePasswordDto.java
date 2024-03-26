package com.archipio.userservice.dto;

import static com.archipio.userservice.util.ValidationUtils.MAX_PASSWORD_LENGTH;
import static com.archipio.userservice.util.ValidationUtils.MIN_PASSWORD_LENGTH;
import static com.archipio.userservice.util.ValidationUtils.PASSWORD_REGEX;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePasswordDto {

  @Schema(description = "Старый пароль", requiredMode = Schema.RequiredMode.REQUIRED)
  private String oldPassword;

  @NotNull(message = "{validation.password.not-null}")
  @Length(
      min = MIN_PASSWORD_LENGTH,
      max = MAX_PASSWORD_LENGTH,
      message = "{validation.password.length}")
  @Pattern(regexp = PASSWORD_REGEX, message = "{validation.password.pattern}")
  @Schema(description = "Новый пароль", requiredMode = Schema.RequiredMode.REQUIRED)
  private String newPassword;
}
