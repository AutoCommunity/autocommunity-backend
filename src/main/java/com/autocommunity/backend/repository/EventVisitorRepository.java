package com.autocommunity.backend.repository;

import com.autocommunity.backend.entity.user.EventVisitor;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface EventVisitorRepository extends CrudRepository<EventVisitor, UUID> {
}
