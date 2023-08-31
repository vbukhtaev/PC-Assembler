package ru.bukhtaev.pcassembler.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.bukhtaev.pcassembler.dto.cross.PsuCpuPowerConnectorDto;
import ru.bukhtaev.pcassembler.dto.cross.PsuGraphicsCardPowerConnectorDto;
import ru.bukhtaev.pcassembler.dto.cross.PsuStoragePowerConnectorDto;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableDto;

import java.util.Date;
import java.util.Set;

@Schema(description = "Блок питания")
@Getter
public class PsuDto extends NameableDto {

    @Min(1)
    @NotNull
    @Schema(description = "Мощность (в Вт)")
    protected Integer power;

    @Min(1)
    @NotNull
    @Schema(description = "Мощность по линии 12V (в Вт)")
    protected Integer power12V;

    @Min(1)
    @NotNull
    @Schema(description = "Длина (в мм)")
    protected Integer length;

    protected NameableDto vendor;

    protected NameableDto formFactor;

    protected NameableDto certificate;

    protected NameableDto mainPowerConnector;

    @Size(min = 1)
    @OneToMany
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<PsuCpuPowerConnectorDto> cpuPowerConnectors;

    @Size(min = 1)
    @OneToMany
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<PsuGraphicsCardPowerConnectorDto> graphicsCardPowerConnectors;

    @Size(min = 1)
    @OneToMany
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<PsuStoragePowerConnectorDto> storagePowerConnectors;

    @Builder
    public PsuDto(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final String name,
            @NotNull final Integer power,
            @NotNull final Integer power12V,
            @NotNull final Integer length,
            final NameableDto vendor,
            final NameableDto formFactor,
            final NameableDto certificate,
            final NameableDto mainPowerConnector,
            final Set<PsuCpuPowerConnectorDto> cpuPowerConnectors,
            final Set<PsuGraphicsCardPowerConnectorDto> graphicsCardPowerConnectors,
            final Set<PsuStoragePowerConnectorDto> storagePowerConnectors
    ) {
        super(id, createdAt, modifiedAt, name);
        this.power = power;
        this.power12V = power12V;
        this.length = length;
        this.vendor = vendor;
        this.formFactor = formFactor;
        this.certificate = certificate;
        this.mainPowerConnector = mainPowerConnector;
        this.cpuPowerConnectors = cpuPowerConnectors;
        this.graphicsCardPowerConnectors = graphicsCardPowerConnectors;
        this.storagePowerConnectors = storagePowerConnectors;
    }
}
