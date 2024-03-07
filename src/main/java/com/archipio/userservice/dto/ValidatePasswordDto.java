package com.archipio.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidatePasswordDto {

  @NotNull(message = "{validation.login.not-null}")
  @Schema(description = "Логин", requiredMode = Schema.RequiredMode.REQUIRED)
  private String login;

  @NotNull(message = "{validation.password.not-null}")
  @Schema(description = "Пароль", requiredMode = Schema.RequiredMode.REQUIRED)
  private String password;
}
