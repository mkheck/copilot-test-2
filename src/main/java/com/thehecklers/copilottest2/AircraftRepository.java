package com.thehecklers.copilottest2;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
//import org.springframework.data.repository.CrudRepository;

public interface AircraftRepository extends ReactiveCrudRepository<Aircraft, String> {
}
