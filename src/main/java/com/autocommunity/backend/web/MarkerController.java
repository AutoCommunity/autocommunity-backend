package com.autocommunity.backend.web;


import com.autocommunity.backend.entity.map.MarkerEntity;
import com.autocommunity.backend.entity.map.MarkerRateEntity;
import com.autocommunity.backend.entity.user.UserEntity;
import com.autocommunity.backend.exception.NotFoundException;
import com.autocommunity.backend.service.MarkerService;
import com.autocommunity.backend.util.AuthContext;
import com.autocommunity.backend.util.UUIDUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping(path = "/api/markers")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MarkerController extends AbstractController {

    private final MarkerService markerService;
    private final AuthContext authContext;

    @GetMapping(path = "/get/all", produces = "application/json")
    public Flux<MarkerDTO> getMarkers() {
        return markerService.getMarkers().map(markerEntity ->
            MarkerDTO.builder()
                .id(markerEntity.getId().toString())
                .name(markerEntity.getName())
                .lat(markerEntity.getLat())
                .lng(markerEntity.getLng())
                .markerType(markerEntity.getMarkerType())
                .build()
        );
    }

    @GetMapping(path = "/get")
    public Mono<MarkerDTOFull> getMarkerById(@RequestParam String markerId) {
        return Mono.just(markerService.getMarkerById(UUIDUtils.parseUUID(markerId))).map(markerEntity ->
            MarkerDTOFull.builder()
                .id(markerEntity.getId().toString())
                .name(markerEntity.getName())
                .lat(markerEntity.getLat())
                .lng(markerEntity.getLng())
                .markerType(markerEntity.getMarkerType())
                .rate(markerEntity.getRates().stream().mapToDouble(MarkerRateEntity::getRate).average().orElse(0))
                .rateCnt(markerEntity.getRates().size())
                .events(markerEntity.getEvents().stream().map(EventController::entityToDTO).collect(Collectors.toSet()))
                .owner(Optional.ofNullable(markerEntity.getOwner()).map(UserEntity::getUsername).orElse(null))
                .build()
        );
    }

    @PostMapping(path = "/add")
    public Mono<ReplyBase> addMarker(@RequestBody @Valid CreateMarkerRequest marker, ServerWebExchange webExchange) {
        return authContext.isUserAuthorised(webExchange)
            .doOnNext(
                session ->
                    markerService.addMarker(marker.getName(), marker.getLat(), marker.getLng(), marker.getMarkerType(), session.getUser())
            ).then(
                Mono.just(ReplyBase.success("marker added"))
            );
    }

    @PostMapping(path = "/rate")
    public Mono<ReplyBase> rateMarker(@RequestBody @Valid RateRequest rateRequest, ServerWebExchange webExchange) {
        return authContext.isUserAuthorised(webExchange)
            .flatMap(session ->
                Mono.defer(
                    () -> {
                        try {
                            markerService.updateRate(
                                markerService.getRate(
                                    markerService.getMarkerById(UUIDUtils.parseUUID(rateRequest.getMarkerId())),
                                    session.getUser()),
                                rateRequest.getRate());
                            return Mono.just(ReplyBase.success("rate updated"));
                        } catch (NotFoundException e) {
                            markerService.addRate(
                                markerService.getMarkerById(UUIDUtils.parseUUID(rateRequest.getMarkerId())),
                                session.getUser(),
                                rateRequest.getRate());
                            return Mono.just(ReplyBase.success("rate added"));
                        }
                    })
            );
    }

    @PostMapping(path= "/remove")
    public Mono<ReplyBase> removeMarker(@RequestParam String markerId, ServerWebExchange webExchange) {
        return authContext.isUserAuthorised(webExchange)
            .doOnNext(
                session ->
                    markerService.removeMarker(UUIDUtils.parseUUID(markerId), session.getUser())
            )
            .then(Mono.just(ReplyBase.success("marker removed")));
    }

    @Getter
    @RequiredArgsConstructor
    public static class RateRequest {
        private final String markerId;

        //@Range(min=0, max=5)
        //todo: validate rate (0, 0.5, 1, ..., 4.5, 5) maybe with custom validator
        private final double rate;
    }

    @Getter
    @RequiredArgsConstructor
    public static class CreateMarkerRequest {
        @NotEmpty
        private final String name;
        @NotNull
        private final MarkerEntity.MarkerType markerType;
        @NotNull
        private final double lat;
        @NotNull
        private final double lng;
    }

    @RequiredArgsConstructor
    @Getter
    @Builder
    private static class MarkerDTO {
        @NotNull
        private final String id;
        @NotEmpty
        private final String name;
        @NotNull
        private final MarkerEntity.MarkerType markerType;
        @NotNull
        private final double lat;
        @NotNull
        private final double lng;
    }

    @RequiredArgsConstructor
    @Getter
    @Builder
    private static class MarkerDTOFull {
        @NotNull
        private final String id;
        @NotEmpty
        private final String name;
        @NotNull
        private final MarkerEntity.MarkerType markerType;
        @NotNull
        private final double lat;
        @NotNull
        private final double lng;
        @NotNull
        private final double rate;
        @NotNull
        private final int rateCnt;
        @NotNull
        private final Set<EventController.EventDTO> events;
        private final String owner;
    }
}
