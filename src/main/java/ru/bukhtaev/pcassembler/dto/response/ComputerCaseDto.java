package ru.bukhtaev.pcassembler.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.bukhtaev.pcassembler.dto.cross.ComputerCaseExpansionBayDto;
import ru.bukhtaev.pcassembler.dto.cross.ComputerCaseFanSizeDto;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableDto;
import ru.bukhtaev.pcassembler.model.dictionary.Vendor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Schema(description = "Корпус")
@Getter
public class ComputerCaseDto extends NameableDto {

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

    protected Vendor vendor;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<NameableDto> supportedMotherboardFormFactors;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<NameableDto> supportedPsuFormFactors;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<ComputerCaseExpansionBayDto> supportedExpansionBays;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<ComputerCaseFanSizeDto> supportedFanSizes;

    @Builder
    public ComputerCaseDto(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final String name,
            @NotNull final Integer psuMaxLength,
            @NotNull final Integer graphicsCardMaxLength,
            @NotNull final Integer coolerMaxHeight,
            final Vendor vendor,
            final Set<NameableDto> supportedMotherboardFormFactors,
            final Set<NameableDto> supportedPsuFormFactors,
            final Set<ComputerCaseExpansionBayDto> supportedExpansionBays,
            final Set<ComputerCaseFanSizeDto> supportedFanSizes
    ) {
        super(id, createdAt, modifiedAt, name);
        this.psuMaxLength = psuMaxLength;
        this.graphicsCardMaxLength = graphicsCardMaxLength;
        this.coolerMaxHeight = coolerMaxHeight;
        this.vendor = vendor;
        this.supportedMotherboardFormFactors = supportedMotherboardFormFactors;
        this.supportedPsuFormFactors = supportedPsuFormFactors;
        this.supportedExpansionBays = supportedExpansionBays;
        this.supportedFanSizes = supportedFanSizes;
    }
}
