package ru.bukhtaev.pcassembler.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bukhtaev.pcassembler.model.Chipset;

import java.util.Optional;

@Repository
public interface ChipsetRepository extends JpaRepository<Chipset, String> {

    Slice<Chipset> findAllBy(final Pageable pageable);

    @Query("SELECT c FROM Chipset c WHERE c.name = :#{#entity.name} AND (:id IS NULL OR c.id <> :id)")
    Optional<Chipset> findAnotherByName(
            @Param("id") final String id,
            @Param("entity") final Chipset entity
    );
}
