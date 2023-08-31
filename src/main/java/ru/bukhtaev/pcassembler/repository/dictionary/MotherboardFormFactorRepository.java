package ru.bukhtaev.pcassembler.repository.dictionary;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bukhtaev.pcassembler.model.dictionary.MotherboardFormFactor;

import java.util.Optional;

@Repository
public interface MotherboardFormFactorRepository extends JpaRepository<MotherboardFormFactor, String> {

    Slice<MotherboardFormFactor> findAllBy(final Pageable pageable);

    @Query("SELECT f FROM MotherboardFormFactor f WHERE f.name = :#{#entity.name} AND (:id IS NULL OR f.id <> :id)")
    Optional<MotherboardFormFactor> findAnotherByName(
            @Param("id") final String id,
            @Param("entity") final MotherboardFormFactor entity
    );
}
