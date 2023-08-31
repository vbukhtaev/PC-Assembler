package ru.bukhtaev.pcassembler.validation.unique;

import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ValidationService {

    @Autowired
    private EntityManager entityManager;

    public abstract boolean fieldValueExists(final Object value, final String fieldName);

    public final boolean check(final Object value, final String fieldName) {
        entityManager.setFlushMode(FlushModeType.COMMIT);
        final boolean exists = fieldValueExists(value, fieldName);
        entityManager.setFlushMode(FlushModeType.AUTO);

        return exists;
    }
}
