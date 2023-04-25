package com.thehecklers.copilottest2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AircraftController.class)
class AircraftControllerTest {
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(AircraftControllerTest.class);

    private final Aircraft ac1 = new Aircraft("12345a", "12345", "PA28");
    private final Iterable<Aircraft> aircraft = List.of(ac1,
            new Aircraft("23456b", "23456", "PA32"),
            new Aircraft("34567c", "34567", "PA46"),
            new Aircraft("45678d", "45678", "C172"),
            new Aircraft("56789e", "56789", "C182"));
    private final Aircraft newAc = new Aircraft("67890f", "67890", "SR22");

    @MockBean
    private AircraftRepository repo;

    @Autowired
    private MockMvc mockMvc;

//    @BeforeEach
//    void setUp() {
//    }

    @Test
    void getAllAircraft() {
        Mockito.when(repo.findAll()).thenReturn(aircraft);

        try {
            mockMvc.perform(get("/aircraft"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", Matchers.hasSize(5)))
                    .andExpect(jsonPath("$[0].adshex", Matchers.is("12345a")))
                    .andExpect(jsonPath("$[0].reg", Matchers.is("12345")))
                    .andExpect(jsonPath("$[0].type", Matchers.is("PA28")))
                    .andDo(print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getAircraftByAdshex() {
        Mockito.when(repo.findById("12345a")).thenReturn(Optional.of(ac1));

        try {
            mockMvc.perform(get("/aircraft/12345a"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.adshex", Matchers.is("12345a")))
                    .andExpect(jsonPath("$.reg", Matchers.is("12345")))
                    .andExpect(jsonPath("$.type", Matchers.is("PA28")))
                    .andDo(print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void addAircraft() {
        Mockito.when(repo.save(newAc)).thenReturn(newAc);

        try {
            mockMvc.perform(post("/aircraft")
                    .contentType(MediaType.APPLICATION_JSON)
//                    .content("{\"adshex\":\"67890f\",\"reg\":\"67890\",\"type\":\"SR22\"}"))
                    .content(new ObjectMapper().writeValueAsString(newAc)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.adshex", Matchers.is("67890f")))
                    .andExpect(jsonPath("$.reg", Matchers.is("67890")))
                    .andExpect(jsonPath("$.type", Matchers.is("SR22")))
                    .andDo(print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}