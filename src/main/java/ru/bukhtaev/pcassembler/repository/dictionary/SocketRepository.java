package ru.bukhtaev.pcassembler.repository.dictionary;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bukhtaev.pcassembler.model.dictionary.Socket;

import java.util.Optional;

@Repository
public interface SocketRepository extends JpaRepository<Socket, String> {

    Slice<Socket> findAllBy(final Pageable pageable);

    @Query("SELECT s FROM Socket s WHERE s.name = :#{#entity.name} AND (:id IS NULL OR s.id <> :id)")
    Optional<Socket> findAnotherByName(
            @Param("id") final String id,
            @Param("entity") final Socket entity
    );
}
