package ru.bukhtaev.pcassembler.model.dictionary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.bukhtaev.pcassembler.model.BaseEntity;

import java.util.Date;

@Entity
@Table(
        name = "fan_size",
        uniqueConstraints = @UniqueConstraint(columnNames = {"length", "width", "height"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FanSize extends BaseEntity {

    @Min(1)
    @NotNull
    @Column(name = "length", nullable = false)
    protected Integer length;

    @Min(1)
    @NotNull
    @Column(name = "width", nullable = false)
    protected Integer width;

    @Min(1)
    @NotNull
    @Column(name = "height", nullable = false)
    protected Integer height;

    @Builder
    public FanSize(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final @NotNull Integer length,
            final @NotNull Integer width,
            final @NotNull Integer height
    ) {
        super(id, createdAt, modifiedAt);
        this.length = length;
        this.width = width;
        this.height = height;
    }
}
