package ru.bukhtaev.pcassembler.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bukhtaev.pcassembler.model.ComputerBuild;

import java.util.Optional;

@Repository
public interface ComputerBuildRepository extends JpaRepository<ComputerBuild, String> {

    Slice<ComputerBuild> findAllBy(final Pageable pageable);

    @Query("SELECT b FROM ComputerBuild b WHERE b.name = :#{#entity.name} AND (:id IS NULL OR b.id <> :id)")
    Optional<ComputerBuild> findAnotherByName(
            @Param("id") final String id,
            @Param("entity") final ComputerBuild entity
    );
}
