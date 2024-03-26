package com.archipio.userservice.unittest.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.archipio.userservice.persistence.entity.Role;
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
  void findByName_whenRoleExists_thenReturnRole() {
    // Prepare
    final var name = "USER";

    final var role = new Role();
    role.setName(name);

    entityManager.persist(role);

    // Do
    var actual = roleRepository.findByName(name);

    // Check
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get()).isEqualTo(role);
  }

  @Test
  void findByName_whenRoleNotExists_thenEmptyOptional() {
    // Prepare
    final var name = "USER";

    // Do
    var actual = roleRepository.findByName(name);

    // Check
    assertThat(actual.isPresent()).isFalse();
  }
}
