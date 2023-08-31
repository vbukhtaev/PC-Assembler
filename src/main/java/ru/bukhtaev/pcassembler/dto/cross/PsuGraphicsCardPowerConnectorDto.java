package ru.bukhtaev.pcassembler.dto.cross;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableDto;
import ru.bukhtaev.pcassembler.dto.response.PsuDto;

@Schema(description = "Количество поддерживаемых блоком питания разъемов питания видеокарты")
@Getter
@Setter
public class PsuGraphicsCardPowerConnectorDto {

    @Schema(description = "ID")
    protected String id;

    @JsonIgnoreProperties({
            "name",
            "createdAt",
            "modifiedAt",
            "power",
            "power12V",
            "length",
            "vendor",
            "formFactor",
            "certificate",
            "mainPowerConnector",
            "cpuPowerConnectors",
            "graphicsCardPowerConnectors",
            "storagePowerConnectors"
    })
    protected PsuDto psu;

    @JsonIgnoreProperties({
            "createdAt",
            "modifiedAt"
    })
    protected NameableDto graphicsCardPowerConnector;

    @Schema(description = "Количество разъемов питания видеокарты")
    @Min(1)
    @NotNull
    protected Integer amount;
}
