package ru.bukhtaev.pcassembler.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.UUID;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableRequestDto;

@Schema(description = "Вентилятор")
@Getter
public class FanRequestDto extends NameableRequestDto {

    @UUID
    @NotBlank
    protected String vendorId;

    @UUID
    @NotBlank
    protected String sizeId;

    @UUID
    @NotBlank
    protected String powerConnectorId;

    @Builder
    public FanRequestDto(
            final String name,
            final String vendorId,
            final String sizeId,
            final String powerConnectorId
    ) {
        super(name);
        this.vendorId = vendorId;
        this.sizeId = sizeId;
        this.powerConnectorId = powerConnectorId;
    }
}
