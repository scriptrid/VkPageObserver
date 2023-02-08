package ru.scriptrid.vkpageobserver.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Validated
public record CreateUserDto(
        @NotBlank
        String username,
        @NotBlank
        @Size(min = 8)
        String password
) {
}
