package com.copycat.app.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RoomControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // Tool for simulating HTTP requests

    @Test
    void createRoom_ShouldReturnStatusCreatedAndRoomCode() throws Exception {
        mockMvc.perform(post("/api/v1/rooms"))
                .andExpect(status().isCreated()) // Expect status 201
                .andExpect(jsonPath("$.roomCode").exists()) // Check that the JSON has a roomCode field
                .andExpect(jsonPath("$.roomCode").isString());
    }

    @Test
    void sendMessage_ShouldReturnBadRequest_WhenContentIsBlank() throws Exception {
        // Create a JSON with an empty message
        String badRequestJson = "{\"content\": \"   \"}";

        mockMvc.perform(post("/api/v1/rooms/testCode/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(badRequestJson))
                .andExpect(status().isBadRequest()) // Expect status 400 (due to @Valid)
                .andExpect(jsonPath("$.content").exists()); // Check that an error for the content field was returned
    }
}