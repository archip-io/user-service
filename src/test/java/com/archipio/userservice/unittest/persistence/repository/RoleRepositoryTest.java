package com.archipio.userservice.unittest.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.archipio.userservice.persistence.entity.Role;
import com.archipio.userservice.persistence.entity.User;
import com.archipio.userservice.persistence.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest(excludeAutoConfiguration = {LiquibaseAutoConfiguration.class})
class RoleRepositoryTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private RoleRepository roleRepository;

  @Test
  void findByName_roleExists_optionalRole() {
    // Prepare
    final var name = "USER";
    final var role = Role.builder()
            .name(name).build();
    entityManager.persist(role);

    // Do
    var actual = roleRepository.findByName(name);

    // Check
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get()).isEqualTo(role);
  }

  @Test
  void findByName_roleNotExists_emptyOptional() {
    // Prepare
    final var name = "USER";

    // Do
    var actual = roleRepository.findByName(name);

    // Check
    assertThat(actual.isPresent()).isFalse();
  }
}
