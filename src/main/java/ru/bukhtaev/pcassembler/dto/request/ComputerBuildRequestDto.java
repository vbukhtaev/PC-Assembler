package ru.bukhtaev.pcassembler.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.UUID;
import ru.bukhtaev.pcassembler.dto.cross.ComputerFanRequestDto;
import ru.bukhtaev.pcassembler.dto.cross.ComputerHddRequestDto;
import ru.bukhtaev.pcassembler.dto.cross.ComputerRamModuleRequestDto;
import ru.bukhtaev.pcassembler.dto.cross.ComputerSsdRequestDto;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableRequestDto;

import java.util.Set;

@Schema(description = "Сборка ПК")
@Getter
public class ComputerBuildRequestDto extends NameableRequestDto {

    @UUID
    @NotBlank
    protected String motherboardId;

    @UUID
    @NotBlank
    protected String cpuId;

    @UUID
    @NotBlank
    protected String coolerId;

    @UUID
    @NotBlank
    protected String graphicsCardId;

    @UUID
    @NotBlank
    protected String psuId;

    @UUID
    @NotBlank
    protected String computerCaseId;

    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<ComputerRamModuleRequestDto> ramModules;

    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<ComputerHddRequestDto> hdds;

    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<ComputerSsdRequestDto> ssds;

    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<ComputerFanRequestDto> fans;

    @Builder
    public ComputerBuildRequestDto(
            final String name,
            final String motherboardId,
            final String cpuId,
            final String coolerId,
            final String graphicsCardId,
            final String psuId,
            final String computerCaseId,
            final Set<ComputerRamModuleRequestDto> ramModules,
            final Set<ComputerHddRequestDto> hdds,
            final Set<ComputerSsdRequestDto> ssds,
            final Set<ComputerFanRequestDto> fans
    ) {
        super(name);
        this.motherboardId = motherboardId;
        this.cpuId = cpuId;
        this.coolerId = coolerId;
        this.graphicsCardId = graphicsCardId;
        this.psuId = psuId;
        this.computerCaseId = computerCaseId;
        this.ramModules = ramModules;
        this.hdds = hdds;
        this.ssds = ssds;
        this.fans = fans;
    }
}
