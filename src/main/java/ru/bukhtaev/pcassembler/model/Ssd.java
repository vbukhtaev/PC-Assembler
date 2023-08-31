package ru.bukhtaev.pcassembler.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
        name = "ssd",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "capacity"})
)
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Ssd extends Storage {

    @Builder
    public Ssd(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final String name,
            final @NotNull Integer capacity,
            final @NotNull Integer readingSpeed,
            final @NotNull Integer writingSpeed,
            final Vendor vendor,
            final StorageConnector connector,
            final StoragePowerConnector powerConnector
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
}
