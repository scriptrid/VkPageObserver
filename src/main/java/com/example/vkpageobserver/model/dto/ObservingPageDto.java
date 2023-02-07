package com.example.vkpageobserver.model.dto;

public record ObservingPageDto(
        String firstName,
        String lastName,
        String bDate,
        String location,
        java.util.List<ChangeDto> changes
) {
}
