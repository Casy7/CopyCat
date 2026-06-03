package com.copycat.app.service;

import com.copycat.app.dto.response.RoomResponse;
import com.copycat.app.dto.response.MessageResponse;
import java.util.List;

public interface RoomService {
    RoomResponse createRoom();
    void validateRoomExists(String roomCode);
    
    // Новий метод для збереження повідомлення
    void addMessageToRoom(String roomCode, String content);
    
    // Новий метод для отримання історії
    List<MessageResponse> getRoomMessages(String roomCode);
}