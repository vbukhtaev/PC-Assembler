package ru.bukhtaev.pcassembler.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import ru.bukhtaev.pcassembler.model.dictionary.Vendor;

import java.util.Date;

@Entity
@Table(
        name = "design",
        uniqueConstraints = @UniqueConstraint(columnNames = "name")
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Design extends NameableEntity {

    @ManyToOne
    @JoinColumn(name = "vendor_id", referencedColumnName = "id", nullable = false)
    protected Vendor vendor;

    @Builder
    public Design(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final String name,
            final Vendor vendor
    ) {
        super(id, createdAt, modifiedAt, name);
        this.vendor = vendor;
    }
}
