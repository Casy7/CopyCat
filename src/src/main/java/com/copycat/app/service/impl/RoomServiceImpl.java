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

    // Constructor Injection (без @Autowired)
    public RoomServiceImpl(InMemoryRoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public RoomResponse createRoom() {
        // Логіка генерації унікального коду з 8 символів
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
        // Знаходимо кімнату або кидаємо помилку
        Room room = roomRepository.findByCode(roomCode)
            .orElseThrow(() -> new RoomNotFoundException("Кімнату з кодом " + roomCode + " не знайдено"));
        
        // Додаємо повідомлення
        room.addMessage(new Message(content));
    }

    @Override
    public List<MessageResponse> getRoomMessages(String roomCode) {
        Room room = roomRepository.findByCode(roomCode)
            .orElseThrow(() -> new RoomNotFoundException("Кімнату з кодом " + roomCode + " не знайдено"));
        
        // Перетворюємо внутрішні Message на MessageResponse за допомогою Stream API
        return room.getMessages().stream()
            .map(msg -> new MessageResponse(msg.getContent(), msg.getTimestamp()))
            .toList();
    }
}