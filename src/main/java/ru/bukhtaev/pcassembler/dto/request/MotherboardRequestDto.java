package ru.bukhtaev.pcassembler.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.UUID;
import ru.bukhtaev.pcassembler.dto.cross.MotherboardStorageConnectorRequestDto;

import java.util.Set;

@Schema(description = "Материнская плата")
@Getter
public class MotherboardRequestDto {

    @Schema(description = "Максимальная частота оперативной памяти (в МГц)")
    @Min(1333)
    @NotNull
    protected Integer maxMemoryClock;

    @Schema(description = "Максимальная частота оперативной памяти с разгоном (в МГц)")
    @Min(1333)
    @NotNull
    protected Integer maxMemoryOverClock;

    @Schema(description = "Количество слотов")
    @Min(1)
    @NotNull
    protected Integer slotsCount;

    @UUID
    @NotBlank
    protected String designId;

    @UUID
    @NotBlank
    protected String chipsetId;

    @UUID
    @NotBlank
    protected String ramTypeId;

    @UUID
    @NotBlank
    protected String formFactorId;

    @UUID
    @NotBlank
    protected String cpuPowerConnectorId;

    @UUID
    @NotBlank
    protected String mainPowerConnectorId;

    @UUID
    @NotBlank
    protected String fanPowerConnectorId;

    @UUID
    @NotBlank
    protected String pciExpressConnectorVersionId;

    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<MotherboardStorageConnectorRequestDto> storageConnectors;

    @Builder
    public MotherboardRequestDto(
            final @NotNull Integer maxMemoryClock,
            final @NotNull Integer maxMemoryOverClock,
            final @NotNull Integer slotsCount,
            final String designId,
            final String chipsetId,
            final String ramTypeId,
            final String formFactorId,
            final String cpuPowerConnectorId,
            final String mainPowerConnectorId,
            final String fanPowerConnectorId,
            final String pciExpressConnectorVersionId,
            final Set<MotherboardStorageConnectorRequestDto> storageConnectors
    ) {
        this.maxMemoryClock = maxMemoryClock;
        this.maxMemoryOverClock = maxMemoryOverClock;
        this.slotsCount = slotsCount;
        this.designId = designId;
        this.chipsetId = chipsetId;
        this.ramTypeId = ramTypeId;
        this.formFactorId = formFactorId;
        this.cpuPowerConnectorId = cpuPowerConnectorId;
        this.mainPowerConnectorId = mainPowerConnectorId;
        this.fanPowerConnectorId = fanPowerConnectorId;
        this.pciExpressConnectorVersionId = pciExpressConnectorVersionId;
        this.storageConnectors = storageConnectors;
    }
}
