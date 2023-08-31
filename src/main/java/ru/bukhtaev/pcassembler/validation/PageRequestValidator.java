package ru.bukhtaev.pcassembler.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

public class PageRequestValidator implements ConstraintValidator<ValidPageable, Pageable> {

    @Override
    public boolean isValid(Pageable pageable, ConstraintValidatorContext constraintValidatorContext) {

        Assert.notNull(pageable, "Page request must not be null");

        return pageable.getPageSize() <= 20;
    }
}
