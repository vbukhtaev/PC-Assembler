package ru.bukhtaev.pcassembler.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "SSD")
@Getter
public class SsdRequestDto extends StorageRequestDto {

    @Builder
    public SsdRequestDto(
            final String name,
            final @NotNull Integer capacity,
            final @NotNull Integer readingSpeed,
            final @NotNull Integer writingSpeed,
            final String vendorId,
            final String connectorId,
            final String powerConnectorId
    ) {
        super(
                name,
                capacity,
                readingSpeed,
                writingSpeed,
                vendorId,
                connectorId,
                powerConnectorId
        );
    }
}
