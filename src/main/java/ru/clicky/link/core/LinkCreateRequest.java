package ru.clicky.link.core;

import jakarta.validation.constraints.NotBlank;

public record LinkCreateRequest(
    @NotBlank String url,
    String alias
) {
}
