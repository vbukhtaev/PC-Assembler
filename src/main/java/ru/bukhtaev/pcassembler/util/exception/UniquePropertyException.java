package ru.bukhtaev.pcassembler.util.exception;

import lombok.Getter;

@Getter
public class UniquePropertyException extends RuntimeException {

    protected final String[] fieldNames;

    public UniquePropertyException(final String message, final String... fieldNames) {
        super(message);
        this.fieldNames = fieldNames;
    }
}
