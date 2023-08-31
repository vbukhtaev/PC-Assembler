package ru.bukhtaev.pcassembler.validation.handling;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
public final class ErrorResponse {

    private final List<Violation> violations;
    private final ZonedDateTime timestamp;

    public ErrorResponse(final Violation violation, ZonedDateTime timestamp) {
        this.violations = Collections.singletonList(violation);
        this.timestamp = timestamp;
    }
}
