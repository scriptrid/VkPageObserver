package com.example.vkpageobserver.model.dto;

import java.time.LocalDate;

public record ObservingPageDto(
        String firstName,
        String lastName,
        LocalDate bDate,
        String location,
        java.util.List<ChangeDto> changes
) {
}
