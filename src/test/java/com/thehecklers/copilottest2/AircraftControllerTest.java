package com.thehecklers.copilottest2;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(AircraftController.class)
//@WebMvcTest(AircraftController.class)
class AircraftControllerTest {
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(AircraftControllerTest.class);

    private final Aircraft ac1 = new Aircraft("12345a", "12345", "PA28");
//    private final Iterable<Aircraft> aircraft = List.of(ac1,
    private final Flux<Aircraft> aircraft = Flux.just(ac1,
            new Aircraft("23456b", "23456", "PA32"),
            new Aircraft("34567c", "34567", "PA46"),
            new Aircraft("45678d", "45678", "C172"),
            new Aircraft("56789e", "56789", "C182"));
    private final Aircraft newAc = new Aircraft("67890f", "67890", "SR22");

    @MockBean
    private AircraftRepository repo;

    @Autowired
//    private MockMvc mockMvc;
    private WebTestClient webclient;

//    @BeforeEach
//    void setUp() {
//    }

    @Test
    void getAllAircraft() {
        Mockito.when(repo.findAll()).thenReturn(aircraft);

        try {
            webclient.get().uri("/aircraft")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBodyList(Aircraft.class)
                    .hasSize(5)
                    .contains(ac1);

//            mockMvc.perform(get("/aircraft"))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$", Matchers.hasSize(5)))
//                    .andExpect(jsonPath("$[0].adshex", Matchers.is("12345a")))
//                    .andExpect(jsonPath("$[0].reg", Matchers.is("12345")))
//                    .andExpect(jsonPath("$[0].type", Matchers.is("PA28")))
//                    .andDo(print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getAircraftByAdshex() {
        Mockito.when(repo.findById("12345a")).thenReturn(Mono.just(ac1));
//        Mockito.when(repo.findById("12345a")).thenReturn(Optional.of(ac1));

        try {
            webclient.get().uri("/aircraft/12345a")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(Aircraft.class)
                    .isEqualTo(ac1);
//            mockMvc.perform(get("/aircraft/12345a"))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$.adshex", Matchers.is("12345a")))
//                    .andExpect(jsonPath("$.reg", Matchers.is("12345")))
//                    .andExpect(jsonPath("$.type", Matchers.is("PA28")))
//                    .andDo(print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void addAircraft() {
        Mockito.when(repo.save(newAc)).thenReturn(Mono.just(newAc));
//        Mockito.when(repo.save(newAc)).thenReturn(newAc);

        try {
            webclient.post().uri("/aircraft")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(newAc), Aircraft.class)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(Aircraft.class)
                    .isEqualTo(newAc);
//            mockMvc.perform(post("/aircraft")
//                    .contentType(MediaType.APPLICATION_JSON)
////                    .content("{\"adshex\":\"67890f\",\"reg\":\"67890\",\"type\":\"SR22\"}"))
//                    .content(new ObjectMapper().writeValueAsString(newAc)))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$.adshex", Matchers.is("67890f")))
//                    .andExpect(jsonPath("$.reg", Matchers.is("67890")))
//                    .andExpect(jsonPath("$.type", Matchers.is("SR22")))
//                    .andDo(print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}