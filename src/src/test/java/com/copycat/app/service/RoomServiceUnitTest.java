package com.copycat.app.service;

import com.copycat.app.dto.response.RoomResponse;
import com.copycat.app.exception.RoomNotFoundException;
import com.copycat.app.model.Room;
import com.copycat.app.repository.InMemoryRoomRepository;
import com.copycat.app.service.impl.RoomServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceUnitTest {

    @Mock
    private InMemoryRoomRepository roomRepository; // Create a "fake" repository

    @InjectMocks
    private RoomServiceImpl roomService; // Inject the fake repo into the real service

    @Test
    void createRoom_ShouldGenerateCodeAndSave() {
        // Action: call the create method
        RoomResponse response = roomService.createRoom();

        // Verification: code is not empty and has 8 characters
        assertNotNull(response);
        assertEquals(8, response.roomCode().length());
        
        // Verification: save() method in the repository was called exactly once
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void validateRoomExists_ShouldThrowException_WhenRoomNotFound() {
        String invalidCode = "error123";
        // Set up mock: return empty when searching for this code
        when(roomRepository.findByCode(invalidCode)).thenReturn(Optional.empty());

        // Verification: check if our custom exception is actually thrown
        assertThrows(RoomNotFoundException.class, () -> {
            roomService.validateRoomExists(invalidCode);
        });
    }
}