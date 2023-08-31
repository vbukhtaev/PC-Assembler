package ru.bukhtaev.pcassembler.repository.dictionary;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bukhtaev.pcassembler.model.dictionary.VideoMemoryType;

import java.util.Optional;

@Repository
public interface VideoMemoryTypeRepository extends JpaRepository<VideoMemoryType, String> {

    Slice<VideoMemoryType> findAllBy(final Pageable pageable);

    @Query("SELECT t FROM VideoMemoryType t WHERE t.name = :#{#entity.name} AND (:id IS NULL OR t.id <> :id)")
    Optional<VideoMemoryType> findAnotherByName(
            @Param("id") final String id,
            @Param("entity") final VideoMemoryType entity
    );
}
