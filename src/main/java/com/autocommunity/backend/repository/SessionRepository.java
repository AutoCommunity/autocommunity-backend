package com.autocommunity.backend.repository;

import com.autocommunity.backend.entity.user.SessionEntity;
import com.autocommunity.backend.entity.user.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionRepository extends PagingAndSortingRepository<SessionEntity, UUID> {

    Optional<SessionEntity> findBySession(String session);
    List<SessionEntity> findAllByExpirationTimeLessThanEqualAndStatus(Date now, SessionEntity.Status status);
    List<SessionEntity> findAllByUserAndStatus(UserEntity user, SessionEntity.Status status);
}