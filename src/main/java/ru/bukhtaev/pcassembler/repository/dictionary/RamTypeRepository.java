package ru.bukhtaev.pcassembler.repository.dictionary;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bukhtaev.pcassembler.model.dictionary.RamType;

import java.util.Optional;

@Repository
public interface RamTypeRepository extends JpaRepository<RamType, String> {

    Slice<RamType> findAllBy(final Pageable pageable);

    @Query("SELECT t FROM RamType t WHERE t.name = :#{#entity.name} AND (:id IS NULL OR t.id <> :id)")
    Optional<RamType> findAnotherByName(
            @Param("id") final String id,
            @Param("entity") final RamType entity
    );
}
