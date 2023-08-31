package ru.bukhtaev.pcassembler.dto.cross;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.UUID;

@Schema(description = "Количество устанавливаемых в ПК SSD")
@Getter
public class ComputerSsdRequestDto {

    @UUID
    @NotBlank
    protected String ssdId;

    @Schema(description = "Количество")
    @Min(1)
    @NotNull
    protected Integer amount;

    @Builder
    public ComputerSsdRequestDto(
            final String ssdId,
            final @NotNull Integer amount
    ) {
        this.ssdId = ssdId;
        this.amount = amount;
    }
}