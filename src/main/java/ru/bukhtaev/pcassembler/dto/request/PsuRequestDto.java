package ru.bukhtaev.pcassembler.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.UUID;
import ru.bukhtaev.pcassembler.dto.cross.*;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableRequestDto;

import java.util.Set;

@Schema(description = "Блок питания")
@Getter
public class PsuRequestDto extends NameableRequestDto {

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

    @UUID
    @NotBlank
    protected String vendorId;

    @UUID
    @NotBlank
    protected String formFactorId;

    @UUID
    @NotBlank
    protected String certificateId;

    @UUID
    @NotBlank
    protected String mainPowerConnectorId;

    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<PsuCpuPowerConnectorRequestDto> cpuPowerConnectors;

    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<PsuGraphicsCardPowerConnectorRequestDto> graphicsCardPowerConnectors;

    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<PsuStoragePowerConnectorRequestDto> storagePowerConnectors;

    @Builder
    public PsuRequestDto(
            final String name,
            final @NotNull Integer power,
            final @NotNull Integer power12V,
            final @NotNull Integer length,
            final String vendorId,
            final String formFactorId,
            final String certificateId,
            final String mainPowerConnectorId,
            final Set<PsuCpuPowerConnectorRequestDto> cpuPowerConnectors,
            final Set<PsuGraphicsCardPowerConnectorRequestDto> graphicsCardPowerConnectors,
            final Set<PsuStoragePowerConnectorRequestDto> storagePowerConnectors
    ) {
        super(name);
        this.power = power;
        this.power12V = power12V;
        this.length = length;
        this.vendorId = vendorId;
        this.formFactorId = formFactorId;
        this.certificateId = certificateId;
        this.mainPowerConnectorId = mainPowerConnectorId;
        this.cpuPowerConnectors = cpuPowerConnectors;
        this.graphicsCardPowerConnectors = graphicsCardPowerConnectors;
        this.storagePowerConnectors = storagePowerConnectors;
    }
}
