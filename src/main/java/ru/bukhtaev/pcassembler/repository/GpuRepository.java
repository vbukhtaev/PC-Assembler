package ru.bukhtaev.pcassembler.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bukhtaev.pcassembler.model.Gpu;

import java.util.Optional;

@Repository
public interface GpuRepository extends JpaRepository<Gpu, String> {

    Slice<Gpu> findAllBy(final Pageable pageable);

    @Query("SELECT u FROM Gpu u " +
            "WHERE u.name = :#{#entity.name} " +
            "AND u.memorySize = :#{#entity.memorySize} " +
            "AND (:id IS NULL OR u.id <> :id)")
    Optional<Gpu> findAnotherByNameAndMemorySize(
            @Param("id") final String id,
            @Param("entity") final Gpu entity
    );
}
