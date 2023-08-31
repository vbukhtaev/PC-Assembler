package ru.bukhtaev.pcassembler.validation.unique;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UniqueValidator implements ConstraintValidator<Unique, String> {

    private ValidationService validationService;
    private String fieldName;

    @Override
    public void initialize(Unique unique) {
        Class<? extends ValidationService> clazz = unique.service();
        this.fieldName = unique.fieldName();
        String serviceQualifier = unique.serviceQualifier();

        if (serviceQualifier.isBlank()) {
            this.validationService = ApplicationContextProvider.getContext().getBean(clazz);
        } else {
            this.validationService = ApplicationContextProvider.getContext().getBean(serviceQualifier, clazz);
        }
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return !this.validationService.check(name, this.fieldName);
    }
}
