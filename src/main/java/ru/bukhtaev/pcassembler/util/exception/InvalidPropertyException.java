package ru.bukhtaev.pcassembler.util.exception;

import lombok.Getter;

@Getter
public class InvalidPropertyException extends RuntimeException {

    protected final String[] fieldNames;

    public InvalidPropertyException(final String message, final String... fieldNames) {
        super(message);
        this.fieldNames = fieldNames;
    }
}
