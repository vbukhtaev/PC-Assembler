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
import ru.bukhtaev.pcassembler.dto.cross.ComputerCaseExpansionBayRequestDto;
import ru.bukhtaev.pcassembler.dto.cross.ComputerCaseFanSizeRequestDto;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableRequestDto;

import java.util.Set;

@Schema(description = "Корпус")
@Getter
public class ComputerCaseRequestDto extends NameableRequestDto {

    @Min(1)
    @NotNull
    @Schema(description = "Максимальная длина блока питания (в мм)")
    protected Integer psuMaxLength;

    @Min(1)
    @NotNull
    @Schema(description = "Максимальная длина видеокарты (в мм)")
    protected Integer graphicsCardMaxLength;

    @Min(1)
    @NotNull
    @Schema(description = "Максимальная высота кулера (в мм)")
    protected Integer coolerMaxHeight;

    @UUID
    @NotBlank
    protected String vendorId;

    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<@UUID @NotBlank String> supportedMotherboardFormFactorIds;

    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<@UUID @NotBlank String> supportedPsuFormFactorIds;

    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<ComputerCaseExpansionBayRequestDto> supportedExpansionBays;

    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<ComputerCaseFanSizeRequestDto> supportedFanSizes;

    @Builder
    public ComputerCaseRequestDto(
            final String name,
            final @NotNull Integer psuMaxLength,
            final @NotNull Integer graphicsCardMaxLength,
            final @NotNull Integer coolerMaxHeight,
            final String vendorId,
            final Set<@UUID @NotBlank String> supportedMotherboardFormFactorIds,
            final Set<@UUID @NotBlank String> supportedPsuFormFactorIds,
            final Set<ComputerCaseExpansionBayRequestDto> supportedExpansionBays,
            final Set<ComputerCaseFanSizeRequestDto> supportedFanSizes
    ) {
        super(name);
        this.psuMaxLength = psuMaxLength;
        this.graphicsCardMaxLength = graphicsCardMaxLength;
        this.coolerMaxHeight = coolerMaxHeight;
        this.vendorId = vendorId;
        this.supportedMotherboardFormFactorIds = supportedMotherboardFormFactorIds;
        this.supportedPsuFormFactorIds = supportedPsuFormFactorIds;
        this.supportedExpansionBays = supportedExpansionBays;
        this.supportedFanSizes = supportedFanSizes;
    }
}
