package ru.bukhtaev.pcassembler.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "HDD")
@Getter
public class HddRequestDto extends StorageRequestDto {

    @Min(5400)
    @NotNull
    @Schema(description = "Скорость вращения шпинделя (в об/мин)")
    protected Integer spindleSpeed;

    @Min(32)
    @NotNull
    @Schema(description = "Объем кэш-памяти (в Мб)")
    protected Integer cacheSize;

    @Builder
    public HddRequestDto(
            final String name,
            final @NotNull Integer capacity,
            final @NotNull Integer readingSpeed,
            final @NotNull Integer writingSpeed,
            final String vendorId,
            final String connectorId,
            final String powerConnectorId,
            final @NotNull Integer spindleSpeed,
            final @NotNull Integer cacheSize
    ) {
        super(
                name,
                capacity,
                readingSpeed,
                writingSpeed,
                vendorId,
                connectorId,
                powerConnectorId
        );
        this.spindleSpeed = spindleSpeed;
        this.cacheSize = cacheSize;
    }
}
