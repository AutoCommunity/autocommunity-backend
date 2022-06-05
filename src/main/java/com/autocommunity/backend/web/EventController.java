package com.autocommunity.backend.web;


import com.autocommunity.backend.service.EventService;
import com.autocommunity.backend.service.MarkerService;
import com.autocommunity.backend.util.AuthContext;
import com.autocommunity.backend.util.UUIDUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000", allowCredentials="true")
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
            .map(event ->
                EventDTO.builder()
                    .id(event.getId().toString())
                    .startDate(event.getStartDate())
                    .endDate(event.getEndDate())
                    .lat(event.getMarker().getLat())
                    .lng(event.getMarker().getLng())
                    .ownerId(event.getOwner().getId().toString())
                    .build()
            );
    }

    @PostMapping(path = "/create")
    public Mono<CreateEventReply> createPublicEvent(@RequestBody @Valid CreateEventRequest request, ServerWebExchange webExchange) {
        return authContext.isUserAuthorised(webExchange)
            .flatMap(session ->
                Mono.defer(
                    () -> Mono.just(eventService.createEvent(
                        markerService.getMarkerById(UUIDUtils.parseUUID(request.getMarkerId())),
                        session.getUser(),
                        request.getPrivacyType()
                    ))
                )
            )
            .map(eventEntity -> new CreateEventReply(eventEntity.getId().toString()));
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
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
        private final Date startDate;
        private final Date endDate;
        private final double lat;
        private final double lng;
        private final String ownerId;
    }

    @Getter
    @RequiredArgsConstructor
    public static class CreateEventRequest {
        @NotEmpty
        private final String markerId;
        @NotEmpty
        private final String privacyType;
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

}
