package ru.bukhtaev.pcassembler.util.exception;

import java.text.MessageFormat;
import java.util.List;

public class InvalidMappersCountException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "Exactly 1 mapper for class {0} should exist, but found: {1}";

    public InvalidMappersCountException(final String message) {
        super(message);
    }

    public InvalidMappersCountException(final Class<?> entityClazz, final List<?> foundMappers) {
        super(
                MessageFormat.format(
                        MESSAGE_TEMPLATE,
                        entityClazz.getSimpleName(),
                        foundMappers.size()
                )
        );
    }
}
