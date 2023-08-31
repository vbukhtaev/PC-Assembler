package ru.bukhtaev.pcassembler.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.UUID;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableRequestDto;

@Schema(description = "Графический процессор")
@Getter
public class GpuRequestDto extends NameableRequestDto {

    @Schema(description = "Объем видеопамяти (в Мб)")
    @Min(1)
    @NotNull
    protected Integer memorySize;

    @Schema(description = "Потребляемая мощность (в Вт)")
    @Min(1)
    @NotNull
    protected Integer powerConsumption;

    @UUID
    @NotBlank
    protected String memoryTypeId;

    @UUID
    @NotBlank
    protected String manufacturerId;

    @Builder
    public GpuRequestDto(
            final String name,
            @NotNull final Integer memorySize,
            @NotNull final Integer powerConsumption,
            final String memoryTypeId,
            final String manufacturerId
    ) {
        super(name);
        this.memorySize = memorySize;
        this.powerConsumption = powerConsumption;
        this.memoryTypeId = memoryTypeId;
        this.manufacturerId = manufacturerId;
    }
}
