package com.autocommunity.backend.repository;

import com.autocommunity.backend.entity.SessionEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionRepository extends PagingAndSortingRepository<SessionEntity, UUID> {

    Optional<SessionEntity> findBySession(String session);

    List<SessionEntity> findAllByExpirationTimeLessThanEqualAndStatus(Date now, SessionEntity.Status status);
}