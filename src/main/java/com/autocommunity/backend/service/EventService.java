package com.autocommunity.backend.service;

import com.autocommunity.backend.entity.map.EventEntity;
import com.autocommunity.backend.entity.map.MarkerEntity;
import com.autocommunity.backend.entity.user.UserEntity;
import com.autocommunity.backend.exception.BadRequestException;
import com.autocommunity.backend.exception.NotFoundException;
import com.autocommunity.backend.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public EventEntity getEventById(UUID id) {
        return eventRepository.findById(id).orElseThrow(() -> new NotFoundException("Event not found"));
    }

    @Transactional(readOnly = true)
    public List<EventEntity> getAllEvents() {
        return eventRepository.findAll();
    }

    @Transactional(rollbackFor = Throwable.class)
    public EventEntity createEvent(MarkerEntity baseMarker, UserEntity user, String name, String description, Date startDate, Date endDate, String privacyType) {
        try {
            var event = EventEntity.builder()
                .marker(baseMarker)
                .owner(user)
                .startDate(startDate)
                .endDate(endDate)
                .status(EventEntity.Status.NOT_STARTED)
                .privacyType(EventEntity.PrivacyType.valueOf(privacyType))
                .name(name)
                .description(description)
                .build();
            return eventRepository.save(event);
        } catch (RuntimeException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteEvent(EventEntity event, UserEntity user) {
        if (!event.isOwnedBy(user)) {
            throw new BadRequestException("User does not own the event");
        }
        event.setStatus(EventEntity.Status.DELETED);
        eventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public Flux<EventEntity> getPublicEventsByMarker(UUID markerId) {
        return Flux.fromIterable(eventRepository.findByMarkerIdAndPrivacyType(markerId, EventEntity.PrivacyType.PUBLIC));
    }

}
