package com.archipio.userservice.unittest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.archipio.userservice.dto.UpdatePasswordDto;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

@JsonTest
public class UpdatePasswordDtoJsonTest {

  @Autowired private JacksonTester<UpdatePasswordDto> updatePasswordDtoJacksonTester;

  @Test
  public void checkSerializationOfUpdatePasswordDto() throws IOException {
    // Prepare
    final var oldPassword = "OldPassword_10";
    final var newPassword = "NewPassword_10";
    final var updatePasswordDto =
        UpdatePasswordDto.builder().oldPassword(oldPassword).newPassword(newPassword).build();

    // Do
    JsonContent<UpdatePasswordDto> result = updatePasswordDtoJacksonTester.write(updatePasswordDto);

    // Check
    assertThat(result).hasJsonPathStringValue("$.old_password");
    assertThat(result).extractingJsonPathStringValue("$.old_password").isEqualTo(oldPassword);
    assertThat(result).hasJsonPathStringValue("$.new_password");
    assertThat(result).extractingJsonPathStringValue("$.new_password").isEqualTo(newPassword);
  }
}
