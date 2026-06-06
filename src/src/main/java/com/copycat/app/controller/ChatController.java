package com.copycat.app.controller;

import com.copycat.app.dto.request.SendMessageRequest;
import com.copycat.app.dto.response.MessageResponse;
import com.copycat.app.service.RoomService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class ChatController {

    private final RoomService roomService;

    public ChatController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Client sends a message to: /app/room/{roomCode}/send
    @MessageMapping("/room/{roomCode}/send")
    // Server takes the method result and automatically broadcasts to everyone listening: /topic/room/{roomCode}
    @SendTo("/topic/room/{roomCode}")
    public MessageResponse sendMessage(
            @DestinationVariable String roomCode, 
            @Payload SendMessageRequest request) {
        
        // 1. Save the message to memory (so it doesn't disappear when the page is refreshed)
        roomService.addMessageToRoom(roomCode, request.content());
        
        // 2. Return the object (Spring will automatically convert it to JSON and send it to subscribers)
        return new MessageResponse(request.content(), LocalDateTime.now());
    }
}