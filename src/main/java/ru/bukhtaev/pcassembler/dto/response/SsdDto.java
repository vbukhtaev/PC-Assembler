package ru.bukhtaev.pcassembler.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableDto;

import java.util.Date;

@Schema(description = "SSD")
@Getter
public class SsdDto extends StorageDto {

    @Builder
    public SsdDto(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final String name,
            @NotNull final Integer capacity,
            @NotNull final Integer readingSpeed,
            @NotNull final Integer writingSpeed,
            final NameableDto vendor,
            final NameableDto connector,
            final NameableDto powerConnector
    ) {
        super(
                id,
                createdAt,
                modifiedAt,
                name,
                capacity,
                readingSpeed,
                writingSpeed,
                vendor,
                connector,
                powerConnector
        );
    }

    public static SsdDto copyOf(final SsdDto source) {
        return SsdDto.builder()
                .id(source.getId())
                .createdAt(source.getCreatedAt())
                .modifiedAt(source.getModifiedAt())
                .name(source.getName())
                .capacity(source.getCapacity())
                .readingSpeed(source.getReadingSpeed())
                .writingSpeed(source.getWritingSpeed())
                .vendor(source.getVendor())
                .connector(source.getConnector())
                .build();
    }
}
