package com.archipio.userservice.dto;


import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class CredentialsOutputDto {

  @Schema(description = "Имя пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
  private String username;

  @Schema(description = "Email", requiredMode = Schema.RequiredMode.REQUIRED)
  private String email;

  @Schema(description = "Полномочия", requiredMode = Schema.RequiredMode.REQUIRED)
  private Set<String> authorities;
}
