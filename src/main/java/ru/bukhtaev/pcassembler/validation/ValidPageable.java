package ru.bukhtaev.pcassembler.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PageRequestValidator.class)
public @interface ValidPageable {

    String message() default "Page size must not exceed 20";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
