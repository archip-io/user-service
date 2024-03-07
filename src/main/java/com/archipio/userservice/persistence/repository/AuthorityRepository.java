package com.archipio.userservice.persistence.repository;

import com.archipio.userservice.persistence.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

  Optional<Authority> findByName(String name);
}
