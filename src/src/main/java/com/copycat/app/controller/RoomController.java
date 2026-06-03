package com.copycat.app.controller;

import com.copycat.app.dto.request.SendMessageRequest;
import com.copycat.app.dto.response.MessageResponse;
import com.copycat.app.dto.response.RoomResponse;
import com.copycat.app.service.RoomService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomService roomService;

    // Constructor Injection
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoomResponse createRoom() {
        return roomService.createRoom();
    }

    @PostMapping("/{roomCode}/messages")
    @ResponseStatus(HttpStatus.OK)
    public void sendMessage(
            @PathVariable String roomCode,
            @Valid @RequestBody SendMessageRequest request) {

        roomService.addMessageToRoom(roomCode, request.content());
    }

    // ОТРИМУЄМО історію повідомлень кімнати
    @GetMapping("/{roomCode}/messages")
    @ResponseStatus(HttpStatus.OK)
    public List<MessageResponse> getMessages(@PathVariable String roomCode) {

        return roomService.getRoomMessages(roomCode);
    }
}