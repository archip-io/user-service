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
  void existsByUsername_userExists_true() {
    // Prepare
    final var username = "username";
    final var email = "email";
    final var password = "password";
    var user = User.builder().username(username).email(email).password(password).build();
    entityManager.persist(user);

    // Do
    var actual = userRepository.existsByUsername(username);

    // Check
    assertThat(actual).isTrue();
  }

  @Test
  void existsByUsername_userNotExists_false() {
    // Prepare
    final var username = "username";

    // Do
    var actual = userRepository.existsByUsername(username);

    // Check
    assertThat(actual).isFalse();
  }

  @Test
  void existsByEmail_userExists_true() {
    // Prepare
    final var username = "username";
    final var email = "email";
    final var password = "password";
    var user = User.builder().username(username).email(email).password(password).build();
    entityManager.persist(user);

    // Do
    var actual = userRepository.existsByEmail(email);

    // Check
    assertThat(actual).isTrue();
  }

  @Test
  void existsByEmail_userNotExists_false() {
    // Prepare
    final var email = "email";

    // Do
    var actual = userRepository.existsByEmail(email);

    // Check
    assertThat(actual).isFalse();
  }

  @Test
  void findByLogin_userExistsAndLoginIsUsername_optionalUser() {
    // Prepare
    final var username = "username";
    final var email = "email";
    final var password = "password";
    var user = User.builder().username(username).email(email).password(password).build();
    entityManager.persist(user);

    // Do
    var actual = userRepository.findByLogin(username);

    // Check
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get()).isEqualTo(user);
  }

  @Test
  void findByLogin_userExistsAndLoginIsEmail_optionalUser() {
    // Prepare
    final var username = "username";
    final var email = "email";
    final var password = "password";
    var user = User.builder().username(username).email(email).password(password).build();
    entityManager.persist(user);

    // Do
    var actual = userRepository.findByLogin(email);

    // Check
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get()).isEqualTo(user);
  }

  @Test
  void findByLogin_userNotExists_emptyOptional() {
    // Prepare
    final var login = "login";

    // Do
    var actual = userRepository.findByLogin(login);

    // Check
    assertThat(actual.isPresent()).isFalse();
  }
}
