package com.autocommunity.backend.web;


import com.autocommunity.backend.entity.map.EventEntity;
import com.autocommunity.backend.service.EventService;
import com.autocommunity.backend.service.MarkerService;
import com.autocommunity.backend.util.AuthContext;
import com.autocommunity.backend.util.UUIDUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@Slf4j
@RequestMapping(path = "/api/event", produces = "application/json")
@RequiredArgsConstructor
@RestController
public class EventController extends AbstractController {

    private final AuthContext authContext;
    private final EventService eventService;
    private final MarkerService markerService;

    @GetMapping(path = "/all")
    public Flux<EventDTO> getAllEvents() {
        return Flux.fromStream(eventService.getAllEvents().stream())
            .map(EventController::entityToDTO);
    }

    @PostMapping(path = "/create")
    public Mono<CreateEventReply> createPublicEvent(@RequestBody @Valid CreateEventRequest request, ServerWebExchange webExchange) {
        return authContext.isUserAuthorised(webExchange)
            .flatMap(session ->
                Mono.defer(
                    () -> Mono.just(eventService.createEvent(
                        markerService.getMarkerById(UUIDUtils.parseUUID(request.getMarkerId())),
                        session.getUser(),
                        request.getName(),
                        request.getDescription(),
                        request.getStartDate(),
                        request.getEndDate(),
                        request.getPrivacyType()
                    ))
                )
            )
            .map(eventEntity -> new CreateEventReply(eventEntity.getId().toString()));
    }

    @GetMapping(path = "/get/marker")
    public Flux<EventDTO> getPublicEventsByMarker(@RequestParam String markerId, ServerWebExchange webExchange) {
        return eventService.getPublicEventsByMarker(UUIDUtils.parseUUID(markerId))
            .map(EventController::entityToDTO);
    }

    @PostMapping("/delete")
    public Mono<ReplyBase> deleteEvent(@RequestBody @Valid DeleteEventRequest request, ServerWebExchange webExchange) {
        return authContext.isUserAuthorised(webExchange)
            .flatMap(session ->
                Mono.defer(
                    () -> {
                        eventService.deleteEvent(
                            eventService.getEventById(UUIDUtils.parseUUID(request.getEventId())),
                            session.getUser()
                        );
                        return Mono.empty();
                    }
                )
            )
            .then(Mono.just(ReplyBase.success("Deleted event")));
    }

    @PostMapping("/visitors/add")
    public Mono<ReplyBase> addVisitors(AddVisitorsRequest request, ServerWebExchange webExchange) {
        //TODO:
        return Mono.empty();
    }

    @Getter
    @Builder
    public static class EventDTO {
        private final String id;
        private final String name;
        private final String description;
        private final Date startDate;
        private final Date endDate;
        private final String markerId;
        private final String ownerId;
    }

    @Getter
    @RequiredArgsConstructor
    public static class CreateEventRequest {
        @NotEmpty
        private final String markerId;
        @NotEmpty
        private final String privacyType;

        @NotEmpty
        private final String name;
        private final String description;

        private final Date startDate;

        private final Date endDate;
    }

    @Getter
    @RequiredArgsConstructor
    public static class CreateEventReply {
        private final String id;
    }

    @Getter
    @Builder
    @Jacksonized
    public static class DeleteEventRequest {
        @NotEmpty
        private final String eventId;
    }


    @Getter
    @RequiredArgsConstructor
    public static class AddVisitorsRequest {
        @NotEmpty
        private final List<String> visitorIds;
    }

    public static EventDTO entityToDTO(EventEntity event) {
        return EventDTO.builder()
            .id(event.getId().toString())
            .startDate(event.getStartDate())
            .endDate(event.getEndDate())
            .markerId(event.getMarker().getId().toString())
            .ownerId(event.getOwner().getId().toString())
            .name(event.getName())
            .description(event.getDescription())
            .build();
    }

}
