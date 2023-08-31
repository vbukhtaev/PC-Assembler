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
import ru.bukhtaev.pcassembler.dto.dictionary.NameableRequestDto;

import java.util.Set;

@Schema(description = "Кулер")
@Getter
public class CoolerRequestDto extends NameableRequestDto {

    @Schema(description = "Рассеиваемая мощность (в Вт)")
    @Min(1)
    @NotNull
    protected Integer powerDissipation;

    @Schema(description = "Высота (в мм)")
    @Min(1)
    @NotNull
    protected Integer height;

    @UUID
    @NotBlank
    protected String vendorId;

    @UUID
    @NotBlank
    protected String fanSizeId;

    @UUID
    @NotBlank
    protected String fanPowerConnectorId;

    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<@UUID @NotBlank String> supportedSocketIds;

    @Builder
    public CoolerRequestDto(
            final String name,
            final @NotNull Integer powerDissipation,
            final @NotNull Integer height,
            final String vendorId,
            final String fanSizeId,
            final String fanPowerConnectorId,
            final Set<String> supportedSocketIds
    ) {
        super(name);
        this.powerDissipation = powerDissipation;
        this.height = height;
        this.vendorId = vendorId;
        this.fanSizeId = fanSizeId;
        this.fanPowerConnectorId = fanPowerConnectorId;
        this.supportedSocketIds = supportedSocketIds;
    }
}
