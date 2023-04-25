package com.thehecklers.copilottest2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@DataMongoTest
class AircraftRepositoryTest {
//    private final Logger log = org.slf4j.LoggerFactory.getLogger(AircraftRepositoryTest.class);

    //    private final Iterable<Aircraft> aircraft = List.of(new Aircraft("12345a", "12345", "PA28"),
    private final Flux<Aircraft> aircraft = Flux.just(new Aircraft("12345a", "12345", "PA28"),
            new Aircraft("23456b", "23456", "PA32"),
            new Aircraft("34567c", "34567", "PA46"),
            new Aircraft("45678d", "45678", "C172"),
            new Aircraft("56789e", "56789", "C182"));

    @Container
    static MongoDBContainer container = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", container::getReplicaSetUrl);
    }

    @Autowired
    private AircraftRepository repo;

    // Before each test, save all aircraft
    @BeforeEach
    void setUpBeforeEach() {
        repo.saveAll(aircraft).subscribe(); //(ac -> System.out.println("Saved: " + ac));
    }

    // After each test, delete all aircraft
    @AfterEach
    void cleanUp() {
        repo.deleteAll().subscribe();
    }

    @Test
    void testAddAircraft() {
        var cirrus = new Aircraft("67890f", "67890", "SR22");
        assertEquals(cirrus, repo.save(new Aircraft("67890f", "67890", "SR22")).block());
    }

    @Test
    void testGetAircraftByAdshex() {
//        assertEquals("PA28",
//                repo.saveAll(aircraft).then(repo.findById("12345a")).block().type());

        repo.findById("12345a")
                .subscribe(ac -> assertEquals("PA28", ac.type()));
    }

    @Test
    void testGetAllAircraft() {
//        assertEquals(5, repo.count());
//        log.info("Aircraft: {}", repo.findAll());

        repo.findAll()
//                .subscribe(ac -> assertEquals(Boolean.TRUE, aircraft.hasElement(ac).block()));
//                or (MH NOTE: Unsure why the above is preferred; functionally equivalent to below)
                .subscribe(ac -> assertTrue(aircraft.hasElement(ac).block()));
    }
}