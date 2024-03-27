package com.archipio.userservice.unittest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.archipio.userservice.dto.CredentialsOutputDto;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

@JsonTest
public class CredentialsOutputDtoTest {

  @Autowired private JacksonTester<CredentialsOutputDto> credentialsOutputDtoJacksonTester;

  @Test
  public void checkSerializationOfCredentialsOutputDto() throws IOException {
    // Prepare
    final var isEnabled = true;
    final var credentialsOutputDto = CredentialsOutputDto.builder().isEnabled(isEnabled).build();

    // Do
    JsonContent<CredentialsOutputDto> result =
        credentialsOutputDtoJacksonTester.write(credentialsOutputDto);

    // Check
    assertThat(result).hasJsonPathBooleanValue("$.is_enabled");
    assertThat(result).extractingJsonPathBooleanValue("$.is_enabled").isEqualTo(isEnabled);
  }
}
