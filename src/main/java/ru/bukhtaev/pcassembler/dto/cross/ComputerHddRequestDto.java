package ru.bukhtaev.pcassembler.dto.cross;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.UUID;

@Schema(description = "Количество устанавливаемых в ПК HDD")
@Getter
public class ComputerHddRequestDto {

    @UUID
    @NotBlank
    protected String hddId;

    @Schema(description = "Количество")
    @Min(1)
    @NotNull
    protected Integer amount;

    @Builder
    public ComputerHddRequestDto(
            final String hddId,
            final @NotNull Integer amount
    ) {
        this.hddId = hddId;
        this.amount = amount;
    }
}