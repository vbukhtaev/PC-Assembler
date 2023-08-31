package ru.bukhtaev.pcassembler.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bukhtaev.pcassembler.model.GraphicsCard;

import java.util.Optional;

@Repository
public interface GraphicsCardRepository extends JpaRepository<GraphicsCard, String> {

    Slice<GraphicsCard> findAllBy(final Pageable pageable);

    @Query("SELECT c FROM GraphicsCard c " +
            "WHERE c.gpu.id = :#{#entity.gpu.id} " +
            "AND c.design.id = :#{#entity.design.id} " +
            "AND (:id IS NULL OR c.id <> :id)")
    Optional<GraphicsCard> findAnotherByGpuAndDesign(
            @Param("id") final String id,
            @Param("entity") final GraphicsCard entity
    );
}
