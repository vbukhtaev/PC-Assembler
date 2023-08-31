package ru.bukhtaev.pcassembler.repository.dictionary;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bukhtaev.pcassembler.model.dictionary.MainPowerConnector;

import java.util.Optional;

@Repository
public interface MainPowerConnectorRepository extends JpaRepository<MainPowerConnector, String> {

    Slice<MainPowerConnector> findAllBy(final Pageable pageable);

    @Query("SELECT с FROM MainPowerConnector с WHERE с.name = :#{#entity.name} AND (:id IS NULL OR с.id <> :id)")
    Optional<MainPowerConnector> findAnotherByName(
            @Param("id") final String id,
            @Param("entity") final MainPowerConnector entity
    );
}
