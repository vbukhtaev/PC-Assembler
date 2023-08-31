package ru.bukhtaev.pcassembler.model.cross;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bukhtaev.pcassembler.model.Motherboard;
import ru.bukhtaev.pcassembler.model.dictionary.StorageConnector;

@Entity
@Table(
        name = "motherboard_to_storage_connector",
        uniqueConstraints = @UniqueConstraint(columnNames = {"motherboard_id", "storage_connector_id"})
)
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class MotherboardStorageConnector {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @ManyToOne
    @JoinColumn(name = "motherboard_id", referencedColumnName = "id", nullable = false)
    protected Motherboard motherboard;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "storage_connector_id", referencedColumnName = "id", nullable = false)
    protected StorageConnector storageConnector;

    @Min(1)
    @NotNull
    @Column(name = "amount", nullable = false)
    protected Integer amount;
}
