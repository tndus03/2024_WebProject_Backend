package com.example.coffee.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.coffee.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
  UserEntity findByUsername(String username);

  Boolean existsByUsername(String username);

  UserEntity findByUsernameAndPassword(String username, String password);
}