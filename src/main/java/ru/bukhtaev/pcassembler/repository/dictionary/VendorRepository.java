package ru.bukhtaev.pcassembler.repository.dictionary;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bukhtaev.pcassembler.model.dictionary.Vendor;

import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, String> {

    Slice<Vendor> findAllBy(final Pageable pageable);

    @Query("SELECT v FROM Vendor v WHERE v.name = :#{#entity.name} AND (:id IS NULL OR v.id <> :id)")
    Optional<Vendor> findAnotherByName(
            @Param("id") final String id,
            @Param("entity") final Vendor entity
    );
}
