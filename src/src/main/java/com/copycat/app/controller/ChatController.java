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

    // Клієнт відправляє повідомлення на: /app/room/{roomCode}/send
    @MessageMapping("/room/{roomCode}/send")
    // Сервер бере результат методу і автоматично розсилає всім, хто слухає: /topic/room/{roomCode}
    @SendTo("/topic/room/{roomCode}")
    public MessageResponse sendMessage(
            @DestinationVariable String roomCode, 
            @Payload SendMessageRequest request) {
        
        // 1. Зберігаємо повідомлення в пам'ять (щоб воно не зникло при оновленні сторінки)
        roomService.addMessageToRoom(roomCode, request.content());
        
        // 2. Повертаємо об'єкт (Spring сам перетворить його на JSON і відправить підписникам)
        return new MessageResponse(request.content(), LocalDateTime.now());
    }
}