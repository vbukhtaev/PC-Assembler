package ru.bukhtaev.pcassembler.dto.cross;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.UUID;

@Schema(description = "Максимальная поддерживаемая процессором частота оперативной памяти")
@Getter
public class CpuRamTypeRequestDto {

    @UUID
    @NotBlank
    protected String ramTypeId;

    @Schema(description = "Максимальная частота оперативной памяти (в МГц)")
    @Min(1333)
    @NotNull
    protected Integer maxMemoryClock;

    @Builder
    public CpuRamTypeRequestDto(
            final String ramTypeId,
            final @NotNull Integer maxMemoryClock
    ) {
        this.ramTypeId = ramTypeId;
        this.maxMemoryClock = maxMemoryClock;
    }
}