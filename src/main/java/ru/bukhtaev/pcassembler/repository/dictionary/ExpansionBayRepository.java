package ru.bukhtaev.pcassembler.repository.dictionary;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bukhtaev.pcassembler.model.dictionary.ExpansionBay;

import java.util.Optional;

@Repository
public interface ExpansionBayRepository extends JpaRepository<ExpansionBay, String> {

    Slice<ExpansionBay> findAllBy(final Pageable pageable);

    @Query("SELECT b FROM ExpansionBay b WHERE b.size = :#{#entity.size} AND (:id IS NULL OR b.id <> :id)")
    Optional<ExpansionBay> findAnotherBySize(
            @Param("id") final String id,
            @Param("entity") final ExpansionBay entity
    );
}
