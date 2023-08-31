package ru.bukhtaev.pcassembler.model.dictionary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.bukhtaev.pcassembler.model.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(
        name = "expansion_bay",
        uniqueConstraints = @UniqueConstraint(columnNames = "size")
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ExpansionBay extends BaseEntity {

    @DecimalMin("1.00")
    @NotNull
    @Column(name = "size", nullable = false)
    protected BigDecimal size;

    public BigDecimal getSize() {
        return this.size == null
                ? null
                : this.size.stripTrailingZeros();
    }

    @Builder
    public ExpansionBay(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final @NotNull BigDecimal size
    ) {
        super(id, createdAt, modifiedAt);
        this.size = size;
    }
}