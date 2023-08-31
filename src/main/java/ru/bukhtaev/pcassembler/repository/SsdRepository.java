package ru.bukhtaev.pcassembler.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bukhtaev.pcassembler.model.Ssd;

import java.util.Optional;

@Repository
public interface SsdRepository extends JpaRepository<Ssd, String> {

    Slice<Ssd> findAllBy(final Pageable pageable);

    @Query("SELECT d FROM Ssd d " +
            "WHERE d.name = :#{#entity.name} " +
            "AND d.capacity = :#{#entity.capacity} " +
            "AND (:id IS NULL OR d.id <> :id)")
    Optional<Ssd> findAnotherByNameAndCapacity(
            @Param("id") final String id,
            @Param("entity") final Ssd entity
    );
}
