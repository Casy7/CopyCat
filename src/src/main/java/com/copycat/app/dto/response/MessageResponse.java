package com.copycat.app.dto.response;

import java.time.LocalDateTime;

public record MessageResponse(
    String content,
    LocalDateTime timestamp
) {}