package com.archipio.userservice.unittest.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.archipio.userservice.persistence.entity.Authority;
import com.archipio.userservice.persistence.repository.AuthorityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest(excludeAutoConfiguration = {LiquibaseAutoConfiguration.class})
class AuthorityRepositoryTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private AuthorityRepository authorityRepository;

  @Test
  void findByName_authorityExists_optionalAuthority() {
    // Prepare
    final var name = "DELETE_USER";
    final var authority = Authority.builder()
            .name(name).build();
    entityManager.persist(authority);

    // Do
    var actual = authorityRepository.findByName(name);

    // Check
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get()).isEqualTo(authority);
  }

  @Test
  void findByName_authorityNotExists_emptyOptional() {
    // Prepare
    final var name = "DELETE_USER";

    // Do
    var actual = authorityRepository.findByName(name);

    // Check
    assertThat(actual.isPresent()).isFalse();
  }
}
