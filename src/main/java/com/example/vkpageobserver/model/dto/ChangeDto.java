package com.example.vkpageobserver.model.dto;

import java.time.LocalDateTime;

public record ChangeDto(
        LocalDateTime timeOfChange,
        String before,
        String after
) {
}
