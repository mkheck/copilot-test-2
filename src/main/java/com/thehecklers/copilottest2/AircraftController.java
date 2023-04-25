package com.thehecklers.copilottest2;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/aircraft")
public class AircraftController {
    private final AircraftRepository repo;

    public AircraftController(AircraftRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public Iterable<Aircraft> getAllAircraft() {
        return repo.findAll();
    }

    @GetMapping("/{adshex}")
    public Optional<Aircraft> getAircraftByAdshex(@PathVariable String adshex) {
        return repo.findById(adshex);
    }

    @PostMapping()
    public Aircraft addAircraft(@RequestBody Aircraft newAircraft) {
        return repo.save(newAircraft);
    }
}
