package com.copycat.app.repository;

import com.copycat.app.model.Room;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryRoomRepository {
    // Ключ - roomCode, Значення - об'єкт Room
    private final ConcurrentHashMap<String, Room> roomStore = new ConcurrentHashMap<>();

    public Room save(Room room) {
        roomStore.put(room.getRoomCode(), room);
        return room;
    }

    public Optional<Room> findByCode(String code) {
        return Optional.ofNullable(roomStore.get(code));
    }
}