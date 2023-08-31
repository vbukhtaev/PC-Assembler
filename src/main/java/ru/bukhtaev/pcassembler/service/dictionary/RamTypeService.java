package ru.bukhtaev.pcassembler.service.dictionary;

import jakarta.validation.Valid;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.bukhtaev.pcassembler.internationalization.Translator;
import ru.bukhtaev.pcassembler.model.dictionary.RamType;
import ru.bukhtaev.pcassembler.repository.dictionary.RamTypeRepository;
import ru.bukhtaev.pcassembler.util.exception.NotFoundException;
import ru.bukhtaev.pcassembler.util.exception.UniqueNameException;
import ru.bukhtaev.pcassembler.validation.ValidPageable;

import java.util.List;
import java.util.Optional;

import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_RAM_TYPE_NOT_FOUND;
import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_RAM_TYPE_UNIQUE_NAME;

@Service
@Validated
public class RamTypeService {

    private final Translator translator;
    private final RamTypeRepository repository;

    @Autowired
    public RamTypeService(
            final Translator translator,
            final RamTypeRepository repository
    ) {
        this.translator = translator;
        this.repository = repository;
    }

    public RamType getById(@UUID final String id) {
        return findById(id);
    }

    public List<RamType> getAll() {
        return repository.findAll();
    }

    public Slice<RamType> getAll(@ValidPageable final Pageable pageable) {
        return repository.findAllBy(pageable);
    }

    public RamType create(@Valid final RamType newRamType) {
        checkNotExists(null, newRamType);
        return repository.save(newRamType);
    }

    public void delete(@UUID final String id) {
        repository.deleteById(id);
    }

    public RamType update(@UUID final String id, final RamType changedRamType) {
        checkNotExists(id, changedRamType);

        final RamType ramTypeToBeUpdated = findById(id);
        Optional.ofNullable(changedRamType.getName())
                .ifPresent(ramTypeToBeUpdated::setName);
        return repository.save(ramTypeToBeUpdated);
    }

    public RamType replace(@UUID final String id, @Valid final RamType newRamType) {
        checkNotExists(id, newRamType);

        final RamType ramType = findById(id);
        newRamType.setId(id);
        newRamType.setCreatedAt(ramType.getCreatedAt());
        return repository.save(newRamType);
    }

    private void checkNotExists(final String id, final RamType ramType) {
        repository.findAnotherByName(id, ramType)
                .ifPresent(entity -> {
                    throw new UniqueNameException(
                            translator.getMessage(
                                    MESSAGE_CODE_RAM_TYPE_UNIQUE_NAME,
                                    entity.getName()
                            )
                    );
                });
    }

    private RamType findById(final String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_RAM_TYPE_NOT_FOUND, id)
                ));
    }
}
