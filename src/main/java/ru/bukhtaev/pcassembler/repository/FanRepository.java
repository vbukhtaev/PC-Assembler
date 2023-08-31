package ru.bukhtaev.pcassembler.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bukhtaev.pcassembler.model.Fan;

import java.util.Optional;

@Repository
public interface FanRepository extends JpaRepository<Fan, String> {

    Slice<Fan> findAllBy(final Pageable pageable);

    @Query("SELECT f FROM Fan f " +
            "WHERE f.name = :#{#entity.name} " +
            "AND f.size.id = :#{#entity.size.id} " +
            "AND (:id IS NULL OR f.id <> :id)")
    Optional<Fan> findAnotherByNameAndSize(
            @Param("id") final String id,
            @Param("entity") final Fan entity
    );
}
