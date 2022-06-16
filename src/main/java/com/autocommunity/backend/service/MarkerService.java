package com.autocommunity.backend.service;


import com.autocommunity.backend.entity.map.MarkerEntity;
import com.autocommunity.backend.entity.map.MarkerRateEntity;
import com.autocommunity.backend.entity.user.UserEntity;
import com.autocommunity.backend.exception.NotFoundException;
import com.autocommunity.backend.repository.MarkerRateRepository;
import com.autocommunity.backend.repository.MarkerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarkerService {

    private final MarkerRepository markerRepository;
    private final MarkerRateRepository markerRateRepository;

    public void addMarker(String name, double lat, double lng, MarkerEntity.MarkerType markerType) {
        var markerEntity = MarkerEntity.builder()
            .name(name)
            .lat(lat)
            .lng(lng)
            .markerType(markerType)
            .build();
        log.info("created marker with id: {}", markerEntity.getId());
        markerRepository.save(markerEntity);
    }

    public MarkerEntity getMarkerById(UUID id) {
        return markerRepository.findById(id).orElseThrow(() -> new NotFoundException("Marker not found"));
    }

    public Flux<MarkerEntity> getMarkers() {
        return Flux.fromIterable(markerRepository.findAll());
    }

    public MarkerRateEntity getRate(MarkerEntity marker, UserEntity user) {
        return markerRateRepository.findByMarkerAndUser(marker, user).orElseThrow(() -> new NotFoundException("rate not found"));
    }

    public void addRate(MarkerEntity marker, UserEntity user, double rate) {
        markerRateRepository.findByMarkerAndUser(marker, user);
        var markerRateEntity = MarkerRateEntity.builder()
            .marker(marker)
            .user(user)
            .rate(rate)
            .build();
        markerRateRepository.save(markerRateEntity);
    }

    public void updateRate(MarkerRateEntity rateEntity, double newRate) {
        rateEntity.setRate(newRate);
        markerRateRepository.save(rateEntity);
    }

}
