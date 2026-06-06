package com.copycat.app.model;

import java.time.LocalDateTime;

public class Message {
    private String content;
    private LocalDateTime timestamp;

    public Message(String content) {
        this.content = content;
        this.timestamp = LocalDateTime.now(); // Automatically record creation time
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}