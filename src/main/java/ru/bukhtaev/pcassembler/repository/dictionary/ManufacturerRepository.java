package ru.bukhtaev.pcassembler.repository.dictionary;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bukhtaev.pcassembler.model.dictionary.Manufacturer;

import java.util.Optional;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, String> {

    Slice<Manufacturer> findAllBy(final Pageable pageable);

    @Query("SELECT m FROM Manufacturer m WHERE m.name = :#{#entity.name} AND (:id IS NULL OR m.id <> :id)")
    Optional<Manufacturer> findAnotherByName(
            @Param("id") final String id,
            @Param("entity") final Manufacturer entity
    );
}
