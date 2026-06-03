package com.copycat.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SendMessageRequest(
    @NotBlank(message = "Повідомлення не може бути порожнім")
    @Size(max = 2000, message = "Повідомлення занадто довге")
    String content
) {}