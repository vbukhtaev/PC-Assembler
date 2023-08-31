package ru.bukhtaev.pcassembler.dto.cross;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.bukhtaev.pcassembler.dto.response.ComputerBuildDto;
import ru.bukhtaev.pcassembler.dto.response.FanDto;

@Schema(description = "Количество устанавливаемых в ПК вентиляторов")
@Getter
@Setter
public class ComputerFanDto {

    @Schema(description = "ID")
    protected String id;

    @JsonIgnoreProperties({
            "name",
            "createdAt",
            "modifiedAt",
            "motherboard",
            "cpu",
            "cooler",
            "graphicsCard",
            "psu",
            "computerCase",
            "ramModules",
            "hdds",
            "ssds",
            "fans"
    })
    protected ComputerBuildDto computer;

    @JsonIgnoreProperties({
            "createdAt",
            "modifiedAt",
            "vendor",
            "size",
            "powerConnector"
    })
    protected FanDto fan;

    @Schema(description = "Количество")
    @Min(1)
    @NotNull
    protected Integer amount;
}
