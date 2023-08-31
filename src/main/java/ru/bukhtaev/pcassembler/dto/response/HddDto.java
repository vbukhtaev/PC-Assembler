package ru.bukhtaev.pcassembler.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableDto;

import java.util.Date;

@Schema(description = "HDD")
@Getter
public class HddDto extends StorageDto {

    @Min(5400)
    @NotNull
    @Schema(description = "Скорость вращения шпинделя (в об/мин)")
    protected Integer spindleSpeed;

    @Min(32)
    @NotNull
    @Schema(description = "Объем кэш-памяти (в Мб)")
    protected Integer cacheSize;

    @Builder
    public HddDto(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final String name,
            @NotNull final Integer capacity,
            @NotNull final Integer readingSpeed,
            @NotNull final Integer writingSpeed,
            final NameableDto vendor,
            final NameableDto connector,
            final NameableDto powerConnector,
            @NotNull final Integer spindleSpeed,
            @NotNull final Integer cacheSize
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
        this.spindleSpeed = spindleSpeed;
        this.cacheSize = cacheSize;
    }

    public static HddDto copyOf(final HddDto source) {
        return HddDto.builder()
                .id(source.getId())
                .createdAt(source.getCreatedAt())
                .modifiedAt(source.getModifiedAt())
                .name(source.getName())
                .capacity(source.getCapacity())
                .readingSpeed(source.getReadingSpeed())
                .writingSpeed(source.getWritingSpeed())
                .spindleSpeed(source.getSpindleSpeed())
                .cacheSize(source.getCacheSize())
                .vendor(source.getVendor())
                .connector(source.getConnector())
                .build();
    }
}
