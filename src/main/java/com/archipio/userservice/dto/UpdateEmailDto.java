package com.archipio.userservice.dto;

import static com.archipio.userservice.util.ValidationUtils.EMAIL_REGEX;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEmailDto {

  @NotNull(message = "{validation.email.not-null}")
  @Email(regexp = EMAIL_REGEX, message = "{validation.email.email}")
  
  private String email;
}
