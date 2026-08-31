package ru.clicky.link.core;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LinkCreateRequest(
    @NotBlank
    @Pattern(
        regexp = "^(https?://)?(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)$",
        message = "Wrong URL format"
    )
    String url,
    String alias
) {
}
