package ru.bukhtaev.pcassembler.repository.dictionary;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bukhtaev.pcassembler.model.dictionary.PsuFormFactor;

import java.util.Optional;

@Repository
public interface PsuFormFactorRepository extends JpaRepository<PsuFormFactor, String> {

    Slice<PsuFormFactor> findAllBy(final Pageable pageable);

    @Query("SELECT f FROM PsuFormFactor f WHERE f.name = :#{#entity.name} AND (:id IS NULL OR f.id <> :id)")
    Optional<PsuFormFactor> findAnotherByName(
            @Param("id") final String id,
            @Param("entity") final PsuFormFactor entity
    );
}
