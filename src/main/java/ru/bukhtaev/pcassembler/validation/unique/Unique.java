package ru.bukhtaev.pcassembler.validation.unique;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueValidator.class)
public @interface Unique {

    String message() default "Value of field <{fieldName}> is not unique";

    String fieldName();

    Class<? extends ValidationService> service();

    String serviceQualifier() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
