package ru.bukhtaev.pcassembler.service;

import jakarta.validation.Valid;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils;
import ru.bukhtaev.pcassembler.internationalization.Translator;
import ru.bukhtaev.pcassembler.model.Chipset;
import ru.bukhtaev.pcassembler.model.dictionary.Socket;
import ru.bukhtaev.pcassembler.repository.ChipsetRepository;
import ru.bukhtaev.pcassembler.repository.dictionary.SocketRepository;
import ru.bukhtaev.pcassembler.util.exception.InvalidPropertyException;
import ru.bukhtaev.pcassembler.util.exception.NotFoundException;
import ru.bukhtaev.pcassembler.util.exception.UniqueNameException;
import ru.bukhtaev.pcassembler.validation.ValidPageable;

import java.util.List;
import java.util.Optional;

import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.*;

@Service
@Validated
public class ChipsetService {

    private final Translator translator;
    private final ChipsetRepository chipsetRepository;
    private final SocketRepository socketRepository;

    @Autowired
    public ChipsetService(
            final Translator translator,
            final ChipsetRepository chipsetRepository,
            final SocketRepository socketRepository
    ) {
        this.translator = translator;
        this.chipsetRepository = chipsetRepository;
        this.socketRepository = socketRepository;
    }

    public Chipset getById(@UUID final String id) {
        return findById(id);
    }

    public List<Chipset> getAll() {
        return chipsetRepository.findAll();
    }

    public Slice<Chipset> getAll(@ValidPageable final Pageable pageable) {
        return chipsetRepository.findAllBy(pageable);
    }

    public Chipset create(@Valid final Chipset newChipset) {
        setRelatedEntities(newChipset);
        checkNotExists(null, newChipset);

        return chipsetRepository.save(newChipset);
    }

    public void delete(@UUID final String id) {
        chipsetRepository.deleteById(id);
    }

    public Chipset update(@UUID final String id, final Chipset changedChipset) {
        final Chipset chipsetToBeUpdated = findById(id);
        updateChangedProperties(chipsetToBeUpdated, changedChipset);
        checkNotExists(id, chipsetToBeUpdated);

        return chipsetRepository.save(chipsetToBeUpdated);
    }

    public Chipset replace(@UUID final String id, @Valid final Chipset newChipset) {
        setRelatedEntities(newChipset);
        checkNotExists(id, newChipset);

        final Chipset chipset = findById(id);
        newChipset.setId(id);
        newChipset.setCreatedAt(chipset.getCreatedAt());
        return chipsetRepository.save(newChipset);
    }

    private void updateChangedProperties(final Chipset chipsetToBeUpdated, final Chipset changedChipset) {
        Optional.ofNullable(changedChipset.getName()).ifPresent(chipsetToBeUpdated::setName);

        final Socket newSocket = changedChipset.getSocket();
        if (newSocket != null && newSocket.getId() != null) {
            final Socket socket = socketRepository.findById(newSocket.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_SOCKET_NOT_FOUND, newSocket.getId())
                    ));
            chipsetToBeUpdated.setSocket(socket);
        }
    }

    private void setRelatedEntities(final Chipset chipset) {
        final Socket newSocket = chipset.getSocket();
        if (newSocket == null || newSocket.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "socket"
            );
        }

        final Socket foundSocket = socketRepository.findById(newSocket.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_SOCKET_NOT_FOUND, newSocket.getId())
                ));
        chipset.setSocket(foundSocket);
    }

    private void checkNotExists(final String id, final Chipset chipset) {
        chipsetRepository.findAnotherByName(id, chipset)
                .ifPresent(entity -> {
                    throw new UniqueNameException(
                            translator.getMessage(
                                    MESSAGE_CODE_CHIPSET_UNIQUE_NAME,
                                    entity.getName()
                            )
                    );
                });
    }

    private Chipset findById(final String id) {
        return chipsetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_CHIPSET_NOT_FOUND, id)
                ));
    }
}
