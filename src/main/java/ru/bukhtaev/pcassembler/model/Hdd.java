package ru.bukhtaev.pcassembler.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.bukhtaev.pcassembler.model.dictionary.StorageConnector;
import ru.bukhtaev.pcassembler.model.dictionary.StoragePowerConnector;
import ru.bukhtaev.pcassembler.model.dictionary.Vendor;

import java.util.Date;

@Entity
@Table(
        name = "hdd",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "capacity"})
)
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Hdd extends Storage {

    @Min(5400)
    @NotNull
    @Column(name = "spindle_speed", nullable = false)
    protected Integer spindleSpeed;

    @Min(32)
    @NotNull
    @Column(name = "cache_size", nullable = false)
    protected Integer cacheSize;

    @Builder
    public Hdd(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final String name,
            final @NotNull Integer capacity,
            final @NotNull Integer readingSpeed,
            final @NotNull Integer writingSpeed,
            final Vendor vendor,
            final StorageConnector connector,
            final StoragePowerConnector powerConnector,
            final @NotNull Integer spindleSpeed,
            final @NotNull Integer cacheSize
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
}
