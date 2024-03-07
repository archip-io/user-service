package com.archipio.userservice.dto;


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

  private String username;
  private String email;
  private Set<String> authorities;
}
