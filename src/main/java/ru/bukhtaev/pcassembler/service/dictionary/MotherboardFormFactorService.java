package ru.bukhtaev.pcassembler.service.dictionary;

import jakarta.validation.Valid;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.bukhtaev.pcassembler.internationalization.Translator;
import ru.bukhtaev.pcassembler.model.dictionary.MotherboardFormFactor;
import ru.bukhtaev.pcassembler.repository.dictionary.MotherboardFormFactorRepository;
import ru.bukhtaev.pcassembler.util.exception.NotFoundException;
import ru.bukhtaev.pcassembler.util.exception.UniqueNameException;
import ru.bukhtaev.pcassembler.validation.ValidPageable;

import java.util.List;
import java.util.Optional;

import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_MOTHERBOARD_FORM_FACTOR_NOT_FOUND;
import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_MOTHERBOARD_FORM_FACTOR_UNIQUE_NAME;

@Service
@Validated
public class MotherboardFormFactorService {

    private final Translator translator;
    private final MotherboardFormFactorRepository repository;

    @Autowired
    public MotherboardFormFactorService(
            final Translator translator,
            final MotherboardFormFactorRepository repository
    ) {
        this.translator = translator;
        this.repository = repository;
    }

    public MotherboardFormFactor getById(@UUID final String id) {
        return findById(id);
    }

    public List<MotherboardFormFactor> getAll() {
        return repository.findAll();
    }

    public Slice<MotherboardFormFactor> getAll(@ValidPageable final Pageable pageable) {
        return repository.findAllBy(pageable);
    }

    public MotherboardFormFactor create(@Valid final MotherboardFormFactor newFormFactor) {
        checkNotExists(null, newFormFactor);
        return repository.save(newFormFactor);
    }

    public void delete(@UUID final String id) {
        repository.deleteById(id);
    }

    public MotherboardFormFactor update(@UUID final String id, final MotherboardFormFactor changedFormFactor) {
        checkNotExists(id, changedFormFactor);

        final MotherboardFormFactor formFactorToBeUpdated = findById(id);
        Optional.ofNullable(changedFormFactor.getName())
                .ifPresent(formFactorToBeUpdated::setName);
        return repository.save(formFactorToBeUpdated);
    }

    public MotherboardFormFactor replace(
            @UUID final String id,
            @Valid final MotherboardFormFactor newFormFactor
    ) {
        checkNotExists(id, newFormFactor);

        final MotherboardFormFactor formFactor = findById(id);
        newFormFactor.setId(id);
        newFormFactor.setCreatedAt(formFactor.getCreatedAt());
        return repository.save(newFormFactor);
    }

    private void checkNotExists(final String id, final MotherboardFormFactor formFactor) {
        repository.findAnotherByName(id, formFactor)
                .ifPresent(entity -> {
                    throw new UniqueNameException(
                            translator.getMessage(
                                    MESSAGE_CODE_MOTHERBOARD_FORM_FACTOR_UNIQUE_NAME,
                                    entity.getName()
                            )
                    );
                });
    }

    private MotherboardFormFactor findById(final String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_MOTHERBOARD_FORM_FACTOR_NOT_FOUND, id)
                ));
    }
}
