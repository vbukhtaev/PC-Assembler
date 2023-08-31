package ru.bukhtaev.pcassembler.dto.cross;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.UUID;

@Schema(description = "Количество устанавливаемых в ПК модулей оперативной памяти")
@Getter
public class ComputerRamModuleRequestDto {

    @UUID
    @NotBlank
    protected String ramModuleId;

    @Schema(description = "Количество")
    @Min(1)
    @NotNull
    protected Integer amount;

    @Builder
    public ComputerRamModuleRequestDto(
            final String ramModuleId,
            final @NotNull Integer amount
    ) {
        this.ramModuleId = ramModuleId;
        this.amount = amount;
    }
}