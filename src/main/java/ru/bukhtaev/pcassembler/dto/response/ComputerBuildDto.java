package ru.bukhtaev.pcassembler.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.bukhtaev.pcassembler.dto.cross.ComputerFanDto;
import ru.bukhtaev.pcassembler.dto.cross.ComputerHddDto;
import ru.bukhtaev.pcassembler.dto.cross.ComputerRamModuleDto;
import ru.bukhtaev.pcassembler.dto.cross.ComputerSsdDto;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableDto;

import java.util.Date;
import java.util.Set;

@Schema(description = "Сборка ПК")
@Getter
public class ComputerBuildDto extends NameableDto {

    @JsonIgnoreProperties("storageConnectors")
    protected MotherboardDto motherboard;

    @JsonIgnoreProperties("supportedRamTypes")
    protected CpuDto cpu;

    @JsonIgnoreProperties("supportedSockets")
    protected CoolerDto cooler;

    protected GraphicsCardDto graphicsCard;

    @JsonIgnoreProperties({
            "graphicsCardPowerConnectors",
            "storagePowerConnectors",
            "cpuPowerConnectors"
    })
    protected PsuDto psu;

    @JsonIgnoreProperties({
            "supportedMotherboardFormFactors",
            "supportedPsuFormFactors",
            "supportedExpansionBays",
            "supportedFanSizes"
    })
    protected ComputerCaseDto computerCase;

    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<ComputerRamModuleDto> ramModules;

    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<ComputerHddDto> hdds;

    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<ComputerSsdDto> ssds;

    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<ComputerFanDto> fans;

    @Builder
    public ComputerBuildDto(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final String name,
            final MotherboardDto motherboard,
            final CpuDto cpu,
            final CoolerDto cooler,
            final GraphicsCardDto graphicsCard,
            final PsuDto psu,
            final ComputerCaseDto computerCase,
            final Set<ComputerRamModuleDto> ramModules,
            final Set<ComputerHddDto> hdds,
            final Set<ComputerSsdDto> ssds,
            final Set<ComputerFanDto> fans
    ) {
        super(id, createdAt, modifiedAt, name);
        this.motherboard = motherboard;
        this.cpu = cpu;
        this.cooler = cooler;
        this.graphicsCard = graphicsCard;
        this.psu = psu;
        this.computerCase = computerCase;
        this.ramModules = ramModules;
        this.hdds = hdds;
        this.ssds = ssds;
        this.fans = fans;
    }
}
