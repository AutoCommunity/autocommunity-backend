package com.autocommunity.backend.web;


import com.autocommunity.backend.entity.MarkerEntity;
import com.autocommunity.backend.repository.MarkerRepository;
import com.autocommunity.backend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "/api/markers")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MarkerController extends AbstractController {

    @RequiredArgsConstructor
    @Getter
    @Builder
    private static class MarkerDTO {
        @NotEmpty
        private final String name;
        @NotNull
        private final MarkerEntity.MarkerType markerType;
        @NotNull
        private final double lat;
        @NotNull
        private final double lng;
    }
    @Autowired
    private MarkerRepository markerRepository;

    @GetMapping(path = "/get", produces = "application/json")
    public Flux<MarkerDTO> getMarkers() {
        return Flux.fromIterable(markerRepository.findAll()).map(markerEntity ->
                MarkerDTO.builder()
                        .name(markerEntity.getName())
                        .lat(markerEntity.getLat())
                        .lng(markerEntity.getLng())
                        .markerType(markerEntity.getMarkerType())
                        .build());
    }

    @PostMapping(path = "/add")
    public Mono<ReplyBase> addMarker(@RequestBody @Valid MarkerDTO marker, ServerWebExchange webExchange){
        System.out.println(marker.getName() + " " + marker.getLat() + " " + marker.getLng());
        var markerEntity = MarkerEntity.builder()
                .name(marker.getName())
                .lat(marker.getLat())
                .lng(marker.getLng())
                .markerType(marker.getMarkerType())
                .build();
        markerRepository.save(markerEntity);
        return Mono.just(ReplyBase.success("marker added"));
    }

}
