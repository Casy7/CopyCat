package com.copycat.app.service.impl;

import com.copycat.app.dto.response.MessageResponse;
import com.copycat.app.dto.response.RoomResponse;
import com.copycat.app.exception.RoomNotFoundException;
import com.copycat.app.model.Message;
import com.copycat.app.model.Room;
import com.copycat.app.repository.InMemoryRoomRepository;
import com.copycat.app.service.RoomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoomServiceImpl implements RoomService {

    private final InMemoryRoomRepository roomRepository;

    // Constructor Injection (without @Autowired)
    public RoomServiceImpl(InMemoryRoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public RoomResponse createRoom() {
        // Logic for generating a unique 8-character code
        String code = UUID.randomUUID().toString().substring(0, 8);
        Room room = new Room(code);
        roomRepository.save(room);
        
        return new RoomResponse(room.getRoomCode());
    }

    @Override
    public void validateRoomExists(String roomCode) {
        roomRepository.findByCode(roomCode)
            .orElseThrow(() -> new RoomNotFoundException("Кімнату з кодом " + roomCode + " не знайдено"));
    }

    @Override
    public void addMessageToRoom(String roomCode, String content) {
        // Find the room or throw an error
        Room room = roomRepository.findByCode(roomCode)
            .orElseThrow(() -> new RoomNotFoundException("Кімнату з кодом " + roomCode + " не знайдено"));
        
        // Add the message
        room.addMessage(new Message(content));
    }

    @Override
    public List<MessageResponse> getRoomMessages(String roomCode) {
        Room room = roomRepository.findByCode(roomCode)
            .orElseThrow(() -> new RoomNotFoundException("Кімнату з кодом " + roomCode + " не знайдено"));
        
        // Convert internal Message objects to MessageResponse using Stream API
        return room.getMessages().stream()
            .map(msg -> new MessageResponse(msg.getContent(), msg.getTimestamp()))
            .toList();
    }
}