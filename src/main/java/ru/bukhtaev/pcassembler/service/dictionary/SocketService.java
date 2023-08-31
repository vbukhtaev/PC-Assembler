package ru.bukhtaev.pcassembler.service.dictionary;

import jakarta.validation.Valid;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.bukhtaev.pcassembler.internationalization.Translator;
import ru.bukhtaev.pcassembler.model.dictionary.Socket;
import ru.bukhtaev.pcassembler.repository.dictionary.SocketRepository;
import ru.bukhtaev.pcassembler.util.exception.NotFoundException;
import ru.bukhtaev.pcassembler.util.exception.UniqueNameException;
import ru.bukhtaev.pcassembler.validation.ValidPageable;

import java.util.List;
import java.util.Optional;

import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_SOCKET_NOT_FOUND;
import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_SOCKET_UNIQUE_NAME;

@Service
@Validated
public class SocketService {

    private final Translator translator;
    private final SocketRepository repository;

    @Autowired
    public SocketService(
            final Translator translator,
            final SocketRepository repository
    ) {
        this.translator = translator;
        this.repository = repository;
    }

    public Socket getById(@UUID final String id) {
        return findById(id);
    }

    public List<Socket> getAll() {
        return repository.findAll();
    }

    public Slice<Socket> getAll(@ValidPageable final Pageable pageable) {
        return repository.findAllBy(pageable);
    }

    public Socket create(@Valid final Socket newSocket) {
        checkNotExists(null, newSocket);
        return repository.save(newSocket);
    }

    public void delete(@UUID final String id) {
        repository.deleteById(id);
    }

    public Socket update(@UUID final String id, final Socket changedSocket) {
        checkNotExists(id, changedSocket);

        final Socket socketToBeUpdated = findById(id);
        Optional.ofNullable(changedSocket.getName())
                .ifPresent(socketToBeUpdated::setName);
        return repository.save(socketToBeUpdated);
    }

    public Socket replace(@UUID final String id, @Valid final Socket newSocket) {
        checkNotExists(id, newSocket);

        final Socket socket = findById(id);
        newSocket.setId(id);
        newSocket.setCreatedAt(socket.getCreatedAt());
        return repository.save(newSocket);
    }

    private void checkNotExists(final String id, final Socket socket) {
        repository.findAnotherByName(id, socket)
                .ifPresent(entity -> {
                    throw new UniqueNameException(
                            translator.getMessage(
                                    MESSAGE_CODE_SOCKET_UNIQUE_NAME,
                                    entity.getName()
                            )
                    );
                });
    }

    private Socket findById(final String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_SOCKET_NOT_FOUND, id)
                ));
    }
}
