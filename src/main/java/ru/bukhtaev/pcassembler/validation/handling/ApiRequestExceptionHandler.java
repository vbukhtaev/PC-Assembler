package ru.bukhtaev.pcassembler.validation.handling;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.bukhtaev.pcassembler.util.exception.InvalidPropertyException;
import ru.bukhtaev.pcassembler.util.exception.NotFoundException;
import ru.bukhtaev.pcassembler.util.exception.UniqueNameException;
import ru.bukhtaev.pcassembler.util.exception.UniquePropertyException;

import java.time.ZonedDateTime;
import java.util.List;

import static ru.bukhtaev.pcassembler.model.BaseEntity.FIELD_ID;
import static ru.bukhtaev.pcassembler.model.NameableEntity.FIELD_NAME;

@Slf4j
@RestControllerAdvice
public class ApiRequestExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onConstraintValidationException(final ConstraintViolationException exception) {
        log.error(exception.getMessage(), exception);
        final List<Violation> violations = exception.getConstraintViolations().stream()
                .map(
                        violation -> new Violation(
                                violation.getMessage(),
                                resolveFieldName(violation)
                        )
                )
                .toList();
        return new ErrorResponse(violations, ZonedDateTime.now());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        log.error(exception.getMessage(), exception);
        final List<Violation> violations = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new Violation(error.getDefaultMessage(), error.getField()))
                .toList();
        return new ErrorResponse(violations, ZonedDateTime.now());
    }


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse onHardwareNotFoundException(final NotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return new ErrorResponse(
                new Violation(
                        exception.getMessage(),
                        FIELD_ID
                ),
                ZonedDateTime.now()
        );
    }

    @ExceptionHandler(UniqueNameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onUniqueNameException(final UniqueNameException exception) {
        log.error(exception.getMessage(), exception);
        return new ErrorResponse(
                new Violation(
                        exception.getMessage(),
                        FIELD_NAME
                ),
                ZonedDateTime.now()
        );
    }

    @ExceptionHandler(InvalidPropertyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onInvalidPropertyException(final InvalidPropertyException exception) {
        log.error(exception.getMessage(), exception);
        return new ErrorResponse(
                new Violation(
                        exception.getMessage(),
                        exception.getFieldNames()
                ),
                ZonedDateTime.now()
        );
    }

    @ExceptionHandler(UniquePropertyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onUniquePropertyException(final UniquePropertyException exception) {
        log.error(exception.getMessage(), exception);
        return new ErrorResponse(
                new Violation(
                        exception.getMessage(),
                        exception.getFieldNames()
                ),
                ZonedDateTime.now()
        );
    }

    private String resolveFieldName(final ConstraintViolation<?> violation) {
        final String proprtyPathString = violation.getPropertyPath().toString();
        return proprtyPathString.substring(
                proprtyPathString.lastIndexOf(".") + 1
        );
    }
}
