package com.copycat.app.model;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String roomCode;
    // List to store the history of messages in this room
    private List<Message> messages;

    public Room(String roomCode) {
        this.roomCode = roomCode;
        this.messages = new ArrayList<>(); // Initialize an empty list on creation
    }

    public String getRoomCode() {
        return roomCode;
    }

    public List<Message> getMessages() {
        return messages;
    }

    // Helper method to add a single message
    public void addMessage(Message message) {
        this.messages.add(message);
    }
}