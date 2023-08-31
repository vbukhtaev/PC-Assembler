package ru.bukhtaev.pcassembler.repository.dictionary;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bukhtaev.pcassembler.model.dictionary.CpuPowerConnector;

import java.util.Optional;

@Repository
public interface CpuPowerConnectorRepository extends JpaRepository<CpuPowerConnector, String> {

    Slice<CpuPowerConnector> findAllBy(final Pageable pageable);

    @Query("SELECT с FROM CpuPowerConnector с WHERE с.name = :#{#entity.name} AND (:id IS NULL OR с.id <> :id)")
    Optional<CpuPowerConnector> findAnotherByName(
            @Param("id") final String id,
            @Param("entity") final CpuPowerConnector entity
    );
}
