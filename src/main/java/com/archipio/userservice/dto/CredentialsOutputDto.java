package com.archipio.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CredentialsOutputDto {

  private String username;

  private String email;

  @JsonProperty("is_enabled")
  private Boolean isEnabled;

  private Set<String> authorities;
}
