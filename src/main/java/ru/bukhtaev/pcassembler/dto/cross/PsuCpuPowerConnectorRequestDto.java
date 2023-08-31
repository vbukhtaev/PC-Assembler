package ru.bukhtaev.pcassembler.dto.cross;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.UUID;

@Schema(description = "Количество поддерживаемых блоком питания разъемов питания процессора")
@Getter
public class PsuCpuPowerConnectorRequestDto {

    @UUID
    @NotBlank
    protected String cpuPowerConnectorId;

    @Schema(description = "Количество разъемов питания процессора")
    @Min(1)
    @NotNull
    protected Integer amount;

    @Builder
    public PsuCpuPowerConnectorRequestDto(
            final String cpuPowerConnectorId,
            final @NotNull Integer amount
    ) {
        this.cpuPowerConnectorId = cpuPowerConnectorId;
        this.amount = amount;
    }
}
