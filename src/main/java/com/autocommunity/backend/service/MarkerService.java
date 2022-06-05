package com.autocommunity.backend.service;


import com.autocommunity.backend.entity.map.MarkerEntity;
import com.autocommunity.backend.repository.MarkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class MarkerService {

    private final MarkerRepository markerRepository;

    public void addMarker(String name, double lat, double lng, MarkerEntity.MarkerType markerType) {
        var markerEntity = MarkerEntity.builder()
            .name(name)
            .lat(lat)
            .lng(lng)
            .markerType(markerType)
            .build();
        markerRepository.save(markerEntity);
    }

    public Flux<MarkerEntity> getMarkers() {
        return Flux.fromIterable(markerRepository.findAll());
    }

}
