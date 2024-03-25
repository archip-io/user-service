package com.archipio.userservice.dto;

import static com.archipio.userservice.util.ValidationUtils.EMAIL_REGEX;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
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
public class UpdateEmailDto {

  @NotNull(message = "{validation.email.not-null}")
  @Email(regexp = EMAIL_REGEX, message = "{validation.email.email}")
  @Schema(description = "Email", requiredMode = Schema.RequiredMode.REQUIRED)
  private String email;
}
