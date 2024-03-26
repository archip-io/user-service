package com.archipio.userservice.unittest.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.archipio.userservice.persistence.entity.User;
import com.archipio.userservice.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest(excludeAutoConfiguration = {LiquibaseAutoConfiguration.class})
class UserRepositoryTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private UserRepository userRepository;

  @Test
  void existsByUsername_whenUserExists_thenReturnTrue() {
    // Prepare
    final var username = "username";
    final var email = "email";
    final var password = "password";
    final var isEnabled = true;

    var user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(password);
    user.setIsEnabled(isEnabled);

    entityManager.persist(user);

    // Do
    var actual = userRepository.existsByUsername(username);

    // Check
    assertThat(actual).isTrue();
  }

  @Test
  void existsByUsername_whenUserNotExists_thenReturnFalse() {
    // Prepare
    final var username = "username";

    // Do
    var actual = userRepository.existsByUsername(username);

    // Check
    assertThat(actual).isFalse();
  }

  @Test
  void existsByEmail_whenUserExists_thenReturnTrue() {
    // Prepare
    final var username = "username";
    final var email = "email";
    final var password = "password";
    final var isEnabled = true;

    var user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(password);
    user.setIsEnabled(isEnabled);

    entityManager.persist(user);

    // Do
    var actual = userRepository.existsByEmail(email);

    // Check
    assertThat(actual).isTrue();
  }

  @Test
  void existsByEmail_whenUserNotExists_thenReturnFalse() {
    // Prepare
    final var email = "email";

    // Do
    var actual = userRepository.existsByEmail(email);

    // Check
    assertThat(actual).isFalse();
  }

  @Test
  void findByLogin_whenUserExistsAndLoginIsUsername_thenReturnUser() {
    // Prepare
    final var username = "username";
    final var email = "email";
    final var password = "password";
    final var isEnabled = true;

    var user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(password);
    user.setIsEnabled(isEnabled);

    entityManager.persist(user);

    // Do
    var actual = userRepository.findByLogin(username);

    // Check
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get()).isEqualTo(user);
  }

  @Test
  void findByLogin_whenUserExistsAndLoginIsEmail_thenReturnUser() {
    // Prepare
    final var username = "username";
    final var email = "email";
    final var password = "password";
    final var isEnabled = true;

    var user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(password);
    user.setIsEnabled(isEnabled);

    entityManager.persist(user);

    // Do
    var actual = userRepository.findByLogin(email);

    // Check
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get()).isEqualTo(user);
  }

  @Test
  void findByLogin_whenUserNotExists_thenEmptyOptional() {
    // Prepare
    final var login = "login";

    // Do
    var actual = userRepository.findByLogin(login);

    // Check
    assertThat(actual.isPresent()).isFalse();
  }

  @Test
  void findByUsernameAndEmail_whenUserExists_thenReturnUser() {
    // Prepare
    final var username = "username";
    final var email = "email";
    final var password = "password";
    final var isEnabled = true;

    var user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(password);
    user.setIsEnabled(isEnabled);

    entityManager.persist(user);

    // Do
    var actual = userRepository.findByUsernameAndEmail(username, email);

    // Check
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get()).isEqualTo(user);
  }

  @Test
  void findByUsernameAndEmail_whenUserNotExists_thenReturnEmptyOptional() {
    // Prepare
    final var username = "username";
    final var email = "email";

    // Do
    var actual = userRepository.findByUsernameAndEmail(username, email);

    // Check
    assertThat(actual.isPresent()).isFalse();
  }

  @Test
  void findByUsername_whenUserExists_thenReturnUser() {
    // Prepare
    final var username = "username";
    final var email = "email";
    final var password = "password";
    final var isEnabled = true;

    var user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(password);
    user.setIsEnabled(isEnabled);

    entityManager.persist(user);

    // Do
    var actual = userRepository.findByUsername(username);

    // Check
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get()).isEqualTo(user);
  }

  @Test
  void findByUsername_whenUserNotExists_thenEmptyOptional() {
    // Prepare
    final var username = "username";

    // Do
    var actual = userRepository.findByUsername(username);

    // Check
    assertThat(actual.isPresent()).isFalse();
  }
}
