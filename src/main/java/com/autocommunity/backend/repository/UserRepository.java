package com.autocommunity.backend.repository;

import com.autocommunity.backend.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsername(String username);
}
