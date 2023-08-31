package ru.bukhtaev.pcassembler.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableDto;

import java.util.Date;

@Schema(description = "Графический процессор")
@Getter
public class GpuDto extends NameableDto {

    @Schema(description = "Объем видеопамяти (в Мб)")
    @Min(1)
    @NotNull
    protected Integer memorySize;

    @Schema(description = "Потребляемая мощность (в Вт)")
    @Min(1)
    @NotNull
    protected Integer powerConsumption;

    protected NameableDto memoryType;

    protected NameableDto manufacturer;

    @Builder
    public GpuDto(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final String name,
            @NotNull final Integer memorySize,
            @NotNull final Integer powerConsumption,
            final NameableDto memoryType,
            final NameableDto manufacturer
    ) {
        super(id, createdAt, modifiedAt, name);
        this.memorySize = memorySize;
        this.powerConsumption = powerConsumption;
        this.memoryType = memoryType;
        this.manufacturer = manufacturer;
    }
}
