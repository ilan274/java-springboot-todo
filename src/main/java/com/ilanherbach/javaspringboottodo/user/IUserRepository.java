package com.ilanherbach.javaspringboottodo.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

// extends JpaRepository passing the model representing and the id type
public interface IUserRepository extends JpaRepository<UserModel, UUID> {
  UserModel findByUsername(String username);
}
