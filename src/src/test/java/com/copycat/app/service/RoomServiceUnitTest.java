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
    private InMemoryRoomRepository roomRepository; // Робимо "фейковий" репозиторій

    @InjectMocks
    private RoomServiceImpl roomService; // Впроваджуємо фейковий репо в реальний сервіс

    @Test
    void createRoom_ShouldGenerateCodeAndSave() {
        // Дія: викликаємо метод створення
        RoomResponse response = roomService.createRoom();

        // Перевірка: код не порожній і має 8 символів
        assertNotNull(response);
        assertEquals(8, response.roomCode().length());
        
        // Перевірка: метод save() у репозиторії був викликаний рівно 1 раз
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void validateRoomExists_ShouldThrowException_WhenRoomNotFound() {
        String invalidCode = "error123";
        // Налаштовуємо мок: коли шукають цей код, повертаємо порожнечу
        when(roomRepository.findByCode(invalidCode)).thenReturn(Optional.empty());

        // Перевірка: чи дійсно викидається наша кастомна помилка
        assertThrows(RoomNotFoundException.class, () -> {
            roomService.validateRoomExists(invalidCode);
        });
    }
}