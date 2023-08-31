package ru.bukhtaev.pcassembler.dto.cross;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.bukhtaev.pcassembler.dto.dictionary.ExpansionBayDto;
import ru.bukhtaev.pcassembler.dto.response.ComputerCaseDto;

@Schema(description = "Количество поддерживаемых корпусом слотов расширения")
@Getter
@Setter
public class ComputerCaseExpansionBayDto {

    @Schema(description = "ID")
    protected String id;

    @JsonIgnoreProperties({
            "name",
            "createdAt",
            "modifiedAt",
            "psuMaxLength",
            "graphicsCardMaxLength",
            "coolerMaxHeight",
            "vendor",
            "supportedMotherboardFormFactors",
            "supportedPsuFormFactors",
            "supportedExpansionBays",
            "supportedFanSizes"
    })
    protected ComputerCaseDto computerCase;

    @JsonIgnoreProperties({
            "createdAt",
            "modifiedAt"
    })
    protected ExpansionBayDto expansionBay;

    @Schema(description = "Количество слотов расширения")
    @Min(1)
    @NotNull
    protected Integer amount;
}
