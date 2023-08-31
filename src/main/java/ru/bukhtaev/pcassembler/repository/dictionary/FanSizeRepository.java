package ru.bukhtaev.pcassembler.repository.dictionary;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bukhtaev.pcassembler.model.dictionary.FanSize;

import java.util.Optional;

@Repository
public interface FanSizeRepository extends JpaRepository<FanSize, String> {

    Slice<FanSize> findAllBy(final Pageable pageable);

    @Query("SELECT s FROM FanSize s " +
            "WHERE s.length = :#{#entity.length} " +
            "AND s.width = :#{#entity.width} " +
            "AND s.height = :#{#entity.height} " +
            "AND (:id IS NULL OR s.id <> :id)")
    Optional<FanSize> findAnotherBySize(
            @Param("id") final String id,
            @Param("entity") final FanSize entity
    );
}
