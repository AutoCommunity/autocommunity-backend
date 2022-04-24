package com.autocommunity.backend.web;


import com.autocommunity.backend.entity.MarkerEntity;
import com.autocommunity.backend.repository.MarkerRepository;
import com.autocommunity.backend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "/api/markers")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MarkerController extends AbstractController {

    @AllArgsConstructor
    @Getter
    private static class MarkerDTO {
        private double lat, lng;
    }
    @Autowired
    private MarkerRepository markerRepository;

    @GetMapping(path = "/get", produces = "application/json")
    public Flux<MarkerDTO> getMarkers() {
        return Flux.fromIterable(markerRepository.findAll()).map(markerEntity -> new MarkerDTO(markerEntity.getLat(), markerEntity.getLng()));
    }

    @PostMapping(path = "/add")
    public Mono<ReplyBase> addMarker(@RequestBody MarkerDTO marker){
        var markerEntity = MarkerEntity.builder()
                        .lat(marker.getLat()).lng(marker.getLng()).build();
        markerRepository.save(markerEntity);
        return Mono.just(ReplyBase.success("marker added"));
    }

}
