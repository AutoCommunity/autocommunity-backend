package com.autocommunity.backend.repository;

import com.autocommunity.backend.entity.map.MarkerEntity;
import com.autocommunity.backend.entity.map.MarkerRateEntity;
import com.autocommunity.backend.entity.user.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface MarkerRateRepository extends CrudRepository<MarkerRateEntity, UUID> {
    Optional<MarkerRateEntity> findByMarkerAndUser(MarkerEntity marker, UserEntity user);
}
