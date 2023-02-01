package com.example.vkpageobserver.model.dto;

import java.time.LocalDate;
import java.util.Set;

public record ObservingPageDto(
        String firstName,
        String lastName,
        LocalDate bDate,
        String location,
        Set<ChangeDto> changes
) {
}
