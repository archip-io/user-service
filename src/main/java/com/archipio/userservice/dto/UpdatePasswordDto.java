package com.archipio.userservice.dto;

import static com.archipio.userservice.util.ValidationUtils.MAX_PASSWORD_LENGTH;
import static com.archipio.userservice.util.ValidationUtils.MIN_PASSWORD_LENGTH;
import static com.archipio.userservice.util.ValidationUtils.PASSWORD_REGEX;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UpdatePasswordDto {

  
  @JsonProperty("old_password")
  private String oldPassword;

  @NotNull(message = "{validation.password.not-null}")
  @Length(
      min = MIN_PASSWORD_LENGTH,
      max = MAX_PASSWORD_LENGTH,
      message = "{validation.password.length}")
  @Pattern(regexp = PASSWORD_REGEX, message = "{validation.password.pattern}")
  
  @JsonProperty("new_password")
  private String newPassword;
}
