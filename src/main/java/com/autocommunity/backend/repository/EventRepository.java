package com.autocommunity.backend.repository;

import com.autocommunity.backend.entity.map.EventEntity;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public interface EventRepository extends CrudRepository<EventEntity, UUID> {
    List<EventEntity> findAllByOrderByStartDateAsc();
    List<EventEntity> findByMarkerIdAndPrivacyTypeOrderByStartDateAsc(@NotNull UUID marker_id, EventEntity.@NotNull PrivacyType privacyType);
}
