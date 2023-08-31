package ru.bukhtaev.pcassembler.dto.cross;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.UUID;

@Schema(description = "Количество поддерживаемых корпусом слотов расширения")
@Getter
public class ComputerCaseExpansionBayRequestDto {

    @UUID
    @NotBlank
    protected String expansionBayId;

    @Schema(description = "Количество слотов расширения")
    @Min(1)
    @NotNull
    protected Integer amount;

    @Builder
    public ComputerCaseExpansionBayRequestDto(
            final String expansionBayId,
            final @NotNull Integer amount
    ) {
        this.expansionBayId = expansionBayId;
        this.amount = amount;
    }
}
