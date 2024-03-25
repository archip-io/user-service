package com.archipio.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ProfileDto {

    @Schema(description = "Имя пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Schema(description = "Email", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "Заблокирован ли пользователь", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("is_enabled")
    private Boolean isEnabled;
}
