package ru.bukhtaev.pcassembler.model;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.bukhtaev.pcassembler.model.dictionary.StorageConnector;
import ru.bukhtaev.pcassembler.model.dictionary.StoragePowerConnector;
import ru.bukhtaev.pcassembler.model.dictionary.Vendor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public abstract class Storage extends NameableEntity {

    @Min(120)
    @NotNull
    @Column(name = "capacity", nullable = false)
    protected Integer capacity;

    @Min(1)
    @NotNull
    @Column(name = "reading_speed", nullable = false)
    protected Integer readingSpeed;

    @Min(1)
    @NotNull
    @Column(name = "writing_speed", nullable = false)
    protected Integer writingSpeed;

    @ManyToOne
    @JoinColumn(name = "vendor_id", referencedColumnName = "id", nullable = false)
    protected Vendor vendor;

    @ManyToOne
    @JoinColumn(name = "connector_id", referencedColumnName = "id", nullable = false)
    protected StorageConnector connector;

    @ManyToOne
    @JoinColumn(name = "power_connector_id", referencedColumnName = "id", nullable = true)
    protected StoragePowerConnector powerConnector;

    protected Storage(
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
        super(id, createdAt, modifiedAt, name);
        this.capacity = capacity;
        this.readingSpeed = readingSpeed;
        this.writingSpeed = writingSpeed;
        this.vendor = vendor;
        this.connector = connector;
        this.powerConnector = powerConnector;
    }
}
