package com.autocommunity.backend.repository;

import com.autocommunity.backend.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findByEmailOrUsername(String email, String username);
}
