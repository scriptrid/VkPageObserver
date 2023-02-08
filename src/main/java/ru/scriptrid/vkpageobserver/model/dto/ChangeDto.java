package ru.scriptrid.vkpageobserver.model.dto;

import java.time.ZonedDateTime;

public record ChangeDto(
        ZonedDateTime timeOfChange,
        String before,
        String after
) {
}
