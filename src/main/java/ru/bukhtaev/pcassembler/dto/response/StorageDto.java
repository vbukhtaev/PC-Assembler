package ru.bukhtaev.pcassembler.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableDto;

import java.util.Date;

@Getter
public abstract class StorageDto extends NameableDto {

    @Min(120)
    @NotNull
    @Schema(description = "Объем (в Гб)")
    protected Integer capacity;

    @Min(1)
    @NotNull
    @Schema(description = "Скорость чтения")
    protected Integer readingSpeed;

    @Min(1)
    @NotNull
    @Schema(description = "Скорость записи")
    protected Integer writingSpeed;

    protected NameableDto vendor;

    protected NameableDto connector;

    protected NameableDto powerConnector;

    protected StorageDto(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final String name,
            @NotNull final Integer capacity,
            @NotNull final Integer readingSpeed,
            @NotNull final Integer writingSpeed,
            final NameableDto vendor,
            final NameableDto connector,
            final NameableDto powerConnector
    ) {
        super(id, createdAt, modifiedAt, name);
        this.capacity = capacity;
        this.readingSpeed = readingSpeed;
        this.writingSpeed = writingSpeed;
        this.vendor = vendor;
        this.connector = connector;
        this.powerConnector = powerConnector;
    }
}
