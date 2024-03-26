package com.archipio.userservice.dto;

import static com.archipio.userservice.util.ValidationUtils.MAX_USERNAME_LENGTH;
import static com.archipio.userservice.util.ValidationUtils.MIN_USERNAME_LENGTH;
import static com.archipio.userservice.util.ValidationUtils.USERNAME_REGEX;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUsernameDto {

  @NotNull(message = "{validation.username.not-null}")
  @Length(
      min = MIN_USERNAME_LENGTH,
      max = MAX_USERNAME_LENGTH,
      message = "{validation.username.length}")
  @Pattern(regexp = USERNAME_REGEX, message = "{validation.username.pattern}")
  @Schema(description = "Имя пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
  private String username;
}
