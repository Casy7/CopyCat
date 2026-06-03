package com.copycat.app.model;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String roomCode;
    // Список для збереження історії повідомлень у цій кімнаті
    private List<Message> messages;

    public Room(String roomCode) {
        this.roomCode = roomCode;
        this.messages = new ArrayList<>(); // Ініціалізуємо порожній список при створенні
    }

    public String getRoomCode() {
        return roomCode;
    }

    public List<Message> getMessages() {
        return messages;
    }

    // Зручний метод для додавання одного повідомлення
    public void addMessage(Message message) {
        this.messages.add(message);
    }
}