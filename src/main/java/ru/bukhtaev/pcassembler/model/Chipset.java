package ru.bukhtaev.pcassembler.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import ru.bukhtaev.pcassembler.model.dictionary.Socket;

import java.util.Date;

@Entity
@Table(
        name = "chipset",
        uniqueConstraints = @UniqueConstraint(columnNames = "name")
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Chipset extends NameableEntity {

    @ManyToOne
    @JoinColumn(name = "socket_id", referencedColumnName = "id", nullable = false)
    protected Socket socket;

    @Builder
    public Chipset(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final String name,
            final Socket socket
    ) {
        super(id, createdAt, modifiedAt, name);
        this.socket = socket;
    }
}
