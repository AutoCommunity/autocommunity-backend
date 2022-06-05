package com.autocommunity.backend.repository;

import com.autocommunity.backend.entity.map.MarkerEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface MarkerRepository extends CrudRepository<MarkerEntity, UUID> {
}
