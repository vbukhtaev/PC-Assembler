package ru.bukhtaev.pcassembler.validation.handling;

import lombok.Getter;

@Getter
public class Violation {

    protected final String message;
    protected final String[] fieldNames;

    public Violation(String message, String... fieldNames) {
        this.message = message;
        this.fieldNames = fieldNames;
    }
}
