package ru.bukhtaev.pcassembler.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bukhtaev.pcassembler.model.Motherboard;

import java.util.Optional;

@Repository
public interface MotherboardRepository extends JpaRepository<Motherboard, String> {

    Slice<Motherboard> findAllBy(final Pageable pageable);

    @Query("SELECT m FROM Motherboard m " +
            "WHERE m.design.id = :#{#entity.design.id} " +
            "AND m.chipset.id = :#{#entity.chipset.id} " +
            "AND m.ramType.id = :#{#entity.ramType.id} " +
            "AND (:id IS NULL OR m.id <> :id)")
    Optional<Motherboard> findAnotherByDesignAndChipsetAndRamType(
            @Param("id") final String id,
            @Param("entity") final Motherboard entity
    );
}
