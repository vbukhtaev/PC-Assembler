package ru.bukhtaev.pcassembler.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.UUID;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableRequestDto;

@Schema(description = "Чипсет")
@Getter
public abstract class StorageRequestDto extends NameableRequestDto {

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

    @UUID
    @NotBlank
    protected String vendorId;

    @UUID
    @NotBlank
    protected String connectorId;

    @UUID
    protected String powerConnectorId;

    protected StorageRequestDto(
            final String name,
            final @NotNull Integer capacity,
            final @NotNull Integer readingSpeed,
            final @NotNull Integer writingSpeed,
            final String vendorId,
            final String connectorId,
            final String powerConnectorId
    ) {
        super(name);
        this.capacity = capacity;
        this.readingSpeed = readingSpeed;
        this.writingSpeed = writingSpeed;
        this.vendorId = vendorId;
        this.connectorId = connectorId;
        this.powerConnectorId = powerConnectorId;
    }
}
