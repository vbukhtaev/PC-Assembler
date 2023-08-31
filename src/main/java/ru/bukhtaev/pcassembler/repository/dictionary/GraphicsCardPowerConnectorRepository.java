package ru.bukhtaev.pcassembler.repository.dictionary;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bukhtaev.pcassembler.model.dictionary.GraphicsCardPowerConnector;

import java.util.Optional;

@Repository
public interface GraphicsCardPowerConnectorRepository extends JpaRepository<GraphicsCardPowerConnector, String> {

    Slice<GraphicsCardPowerConnector> findAllBy(final Pageable pageable);

    @Query("SELECT с FROM GraphicsCardPowerConnector с WHERE с.name = :#{#entity.name} " +
            "AND (:id IS NULL OR с.id <> :id)")
    Optional<GraphicsCardPowerConnector> findAnotherByName(
            @Param("id") final String id,
            @Param("entity") final GraphicsCardPowerConnector entity
    );
}
