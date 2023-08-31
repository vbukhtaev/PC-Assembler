package ru.bukhtaev.pcassembler.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.bukhtaev.pcassembler.dto.cross.MotherboardStorageConnectorDto;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableDto;

import java.util.Date;
import java.util.Set;

@Schema(description = "Материнская плата")
@Getter
public class MotherboardDto extends BaseDto {

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

    protected DesignDto design;

    protected ChipsetDto chipset;

    protected NameableDto ramType;

    protected NameableDto formFactor;

    protected NameableDto cpuPowerConnector;

    protected NameableDto mainPowerConnector;

    protected NameableDto fanPowerConnector;

    protected NameableDto pciExpressConnectorVersion;

    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<MotherboardStorageConnectorDto> storageConnectors;

    @Builder
    public MotherboardDto(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final @NotNull Integer maxMemoryClock,
            final @NotNull Integer maxMemoryOverClock,
            final @NotNull Integer slotsCount,
            final DesignDto design,
            final ChipsetDto chipset,
            final NameableDto ramType,
            final NameableDto formFactor,
            final NameableDto cpuPowerConnector,
            final NameableDto mainPowerConnector,
            final NameableDto fanPowerConnector,
            final NameableDto pciExpressConnectorVersion,
            final Set<MotherboardStorageConnectorDto> storageConnectors
    ) {
        super(id, createdAt, modifiedAt);
        this.maxMemoryClock = maxMemoryClock;
        this.maxMemoryOverClock = maxMemoryOverClock;
        this.slotsCount = slotsCount;
        this.design = design;
        this.chipset = chipset;
        this.ramType = ramType;
        this.formFactor = formFactor;
        this.cpuPowerConnector = cpuPowerConnector;
        this.mainPowerConnector = mainPowerConnector;
        this.fanPowerConnector = fanPowerConnector;
        this.pciExpressConnectorVersion = pciExpressConnectorVersion;
        this.storageConnectors = storageConnectors;
    }
}
