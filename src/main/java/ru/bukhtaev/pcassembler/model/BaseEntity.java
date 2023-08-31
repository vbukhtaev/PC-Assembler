package ru.bukhtaev.pcassembler.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    public static final String FIELD_ID = "id";

    @Id
    @UUID
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP", name = "created_at")
    protected Date createdAt;

    @LastModifiedDate
    @Column(columnDefinition = "TIMESTAMP", name = "modified_at")
    protected Date modifiedAt;

    protected BaseEntity(String id) {
        this.id = id;
    }
}
