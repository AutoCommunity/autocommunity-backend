package com.autocommunity.backend.web;


import com.autocommunity.backend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RequestMapping(path = "/api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MarkerController extends AbstractController {

    @AllArgsConstructor
    @Getter
    private static class MockMarker {
        double lat, lng;

    }

    private final UserService userService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "/getMarkers", produces = "application/json")
    public Flux<MockMarker> getMarkers() {
        return Flux.just(
                new MockMarker(50.266258, 19.8415741),
                new MockMarker(51.266258, 20.8415741),
                new MockMarker(49.266258, 19.8415741),
                new MockMarker(49.666258, 19.4415741));
    }

}
