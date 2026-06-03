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
    private MockMvc mockMvc; // Інструмент для імітації HTTP запитів

    @Test
    void createRoom_ShouldReturnStatusCreatedAndRoomCode() throws Exception {
        mockMvc.perform(post("/api/v1/rooms"))
                .andExpect(status().isCreated()) // Очікуємо статус 201
                .andExpect(jsonPath("$.roomCode").exists()) // Перевіряємо, що в JSON є поле roomCode
                .andExpect(jsonPath("$.roomCode").isString());
    }

    @Test
    void sendMessage_ShouldReturnBadRequest_WhenContentIsBlank() throws Exception {
        // Формуємо JSON з порожнім повідомленням
        String badRequestJson = "{\"content\": \"   \"}";

        mockMvc.perform(post("/api/v1/rooms/testCode/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(badRequestJson))
                .andExpect(status().isBadRequest()) // Очікуємо статус 400 (через @Valid)
                .andExpect(jsonPath("$.content").exists()); // Перевіряємо, що повернулася помилка саме поля content
    }
}