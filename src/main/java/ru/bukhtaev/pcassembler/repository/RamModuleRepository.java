package ru.bukhtaev.pcassembler.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bukhtaev.pcassembler.model.RamModule;

import java.util.Optional;

@Repository
public interface RamModuleRepository extends JpaRepository<RamModule, String> {

    Slice<RamModule> findAllBy(final Pageable pageable);

    @Query("SELECT m FROM RamModule m " +
            "WHERE m.clock = :#{#entity.clock} " +
            "AND m.capacity = :#{#entity.capacity} " +
            "AND m.type.id = :#{#entity.type.id} " +
            "AND m.design.id = :#{#entity.design.id} " +
            "AND (:id IS NULL OR m.id <> :id)")
    Optional<RamModule> findByClockAndCapacityAndTypeAndDesign(
            @Param("id") final String id,
            @Param("entity") final RamModule entity
    );
}
