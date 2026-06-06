package com.copycat.app.service;

import com.copycat.app.dto.response.RoomResponse;
import com.copycat.app.dto.response.MessageResponse;
import java.util.List;

public interface RoomService {
    RoomResponse createRoom();
    void validateRoomExists(String roomCode);
    
    // New method for saving a message
    void addMessageToRoom(String roomCode, String content);
    
    // New method for getting history
    List<MessageResponse> getRoomMessages(String roomCode);
}