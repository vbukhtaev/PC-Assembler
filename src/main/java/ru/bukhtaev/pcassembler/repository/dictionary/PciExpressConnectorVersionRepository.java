package ru.bukhtaev.pcassembler.repository.dictionary;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bukhtaev.pcassembler.model.dictionary.PciExpressConnectorVersion;

import java.util.Optional;

@Repository
public interface PciExpressConnectorVersionRepository extends JpaRepository<PciExpressConnectorVersion, String> {

    Slice<PciExpressConnectorVersion> findAllBy(final Pageable pageable);

    @Query("SELECT v FROM PciExpressConnectorVersion v WHERE v.name = :#{#entity.name} " +
            "AND (:id IS NULL OR v.id <> :id)")
    Optional<PciExpressConnectorVersion> findAnotherByName(
            @Param("id") final String id,
            @Param("entity") final PciExpressConnectorVersion entity
    );
}
