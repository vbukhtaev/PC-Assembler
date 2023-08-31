package ru.bukhtaev.pcassembler.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public abstract class NameableEntity extends BaseEntity {

    public static final String FIELD_NAME = "name";

    @NotBlank
    @Column(name = "name", length = 64, nullable = false, unique = true)
    protected String name;

    protected NameableEntity(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final String name
    ) {
        super(id, createdAt, modifiedAt);
        this.name = name;
    }
}
