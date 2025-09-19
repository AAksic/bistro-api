package de.project.test.bistro_api.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Builder
@Getter
public class ErrorResponse {
    private final String exceptionType;
    private final String message;
    private final Instant timestamp = Instant.now();
}
