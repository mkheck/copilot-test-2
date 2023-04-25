package com.thehecklers.copilottest2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@DataMongoTest
class AircraftRepositoryTest {
//    private final Logger log = org.slf4j.LoggerFactory.getLogger(AircraftRepositoryTest.class);

    private final Iterable<Aircraft> aircraft = List.of(new Aircraft("12345a", "12345", "PA28"),
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
        repo.saveAll(aircraft);
    }

    // After each test, delete all aircraft
    @AfterEach
    void cleanUp() {
        repo.deleteAll();
    }

    @Test
    void testAddAircraft() {
        var cirrus = new Aircraft("67890f", "67890", "SR22");
        assertEquals(cirrus, repo.save(new Aircraft("67890f", "67890", "SR22")));
    }

    @Test
    void testGetAircraftByAdshex() {
        assertEquals("PA28", repo.findById("12345a").get().type());
    }

    @Test
    void testGetAllAircraft() {
//        assertEquals(5, repo.count());
//        log.info("Aircraft: {}", repo.findAll());
        assertEquals(aircraft, repo.findAll());
    }
}