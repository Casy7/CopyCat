package com.copycat.app.model;

import java.time.LocalDateTime;

public class Message {
    private String content;
    private LocalDateTime timestamp;

    public Message(String content) {
        this.content = content;
        this.timestamp = LocalDateTime.now(); // Автоматично фіксуємо час створення
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}