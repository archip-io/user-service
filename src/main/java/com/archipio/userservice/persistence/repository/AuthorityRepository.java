package com.archipio.userservice.persistence.repository;

import com.archipio.userservice.persistence.entity.Authority;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

  Optional<Authority> findByName(String name);
}
