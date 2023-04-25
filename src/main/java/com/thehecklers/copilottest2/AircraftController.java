package com.thehecklers.copilottest2;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/aircraft")
public class AircraftController {
    private final AircraftRepository repo;

    public AircraftController(AircraftRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public Flux<Aircraft> getAllAircraft() {
//    public Iterable<Aircraft> getAllAircraft() {
        return repo.findAll();
    }

    @GetMapping("/{adshex}")
    public Mono<Aircraft> getAircraftByAdshex(@PathVariable String adshex) {
//    public Optional<Aircraft> getAircraftByAdshex(@PathVariable String adshex) {
        return repo.findById(adshex);
    }

    @PostMapping()
    public Mono<Aircraft> addAircraft(@RequestBody Aircraft newAircraft) {
//    public Aircraft addAircraft(@RequestBody Aircraft newAircraft) {
        return repo.save(newAircraft);
    }
}
