package com.archipio.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
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

  @Schema(description = "Заблокирован ли пользователь", requiredMode = Schema.RequiredMode.REQUIRED)
  private boolean isEnabled;

  @Schema(description = "Полномочия", requiredMode = Schema.RequiredMode.REQUIRED)
  private Set<String> authorities;
}
