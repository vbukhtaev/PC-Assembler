package ru.bukhtaev.pcassembler.dto.cross;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableDto;
import ru.bukhtaev.pcassembler.dto.response.MotherboardDto;

@Schema(description = "Количество поддерживаемых материнской платой разъемов накопителей")
@Getter
@Setter
public class MotherboardStorageConnectorDto {

    @Schema(description = "ID")
    protected String id;

    @JsonIgnoreProperties({
            "createdAt",
            "modifiedAt",
            "maxMemoryClock",
            "maxMemoryOverClock",
            "slotsCount",
            "design",
            "chipset",
            "ramType",
            "formFactor",
            "cpuPowerConnector",
            "mainPowerConnector",
            "fanPowerConnector",
            "pciExpressConnectorVersion",
            "storageConnectors"
    })
    protected MotherboardDto motherboard;

    @JsonIgnoreProperties({
            "createdAt",
            "modifiedAt"
    })
    protected NameableDto storageConnector;

    @Schema(description = "Количество разъемов накопителей")
    @Min(1)
    @NotNull
    protected Integer amount;
}
