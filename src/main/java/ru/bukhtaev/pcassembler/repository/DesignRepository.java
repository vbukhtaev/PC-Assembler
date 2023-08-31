package ru.bukhtaev.pcassembler.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bukhtaev.pcassembler.model.Design;

import java.util.Optional;

@Repository
public interface DesignRepository extends JpaRepository<Design, String> {

    Slice<Design> findAllBy(final Pageable pageable);

    @Query("SELECT d FROM Design d WHERE d.name = :#{#entity.name} AND (:id IS NULL OR d.id <> :id)")
    Optional<Design> findAnotherByName(
            @Param("id") final String id,
            @Param("entity") final Design entity
    );
}
