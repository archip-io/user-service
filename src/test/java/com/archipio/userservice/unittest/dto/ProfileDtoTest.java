package com.archipio.userservice.unittest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.archipio.userservice.dto.ProfileDto;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

@JsonTest
public class ProfileDtoTest {

  @Autowired private JacksonTester<ProfileDto> profileDtoJacksonTester;

  @Test
  public void checkSerializationOfProfileDto() throws IOException {
    // Prepare
    final var isEnabled = true;
    final var profileDto = ProfileDto.builder().isEnabled(isEnabled).build();

    // Do
    JsonContent<ProfileDto> result = profileDtoJacksonTester.write(profileDto);

    // Check
    assertThat(result).hasJsonPathBooleanValue("$.is_enabled");
    assertThat(result).extractingJsonPathBooleanValue("$.is_enabled").isEqualTo(isEnabled);
  }
}
