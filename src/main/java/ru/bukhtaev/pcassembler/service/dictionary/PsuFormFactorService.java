package ru.bukhtaev.pcassembler.service.dictionary;

import jakarta.validation.Valid;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.bukhtaev.pcassembler.internationalization.Translator;
import ru.bukhtaev.pcassembler.model.dictionary.PsuFormFactor;
import ru.bukhtaev.pcassembler.repository.dictionary.PsuFormFactorRepository;
import ru.bukhtaev.pcassembler.util.exception.NotFoundException;
import ru.bukhtaev.pcassembler.util.exception.UniqueNameException;
import ru.bukhtaev.pcassembler.validation.ValidPageable;

import java.util.List;
import java.util.Optional;

import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_PSU_FORM_FACTOR_NOT_FOUND;
import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_PSU_FORM_FACTOR_UNIQUE_NAME;

@Service
@Validated
public class PsuFormFactorService {

    private final Translator translator;
    private final PsuFormFactorRepository repository;

    @Autowired
    public PsuFormFactorService(
            final Translator translator,
            final PsuFormFactorRepository repository
    ) {
        this.translator = translator;
        this.repository = repository;
    }

    public PsuFormFactor getById(@UUID final String id) {
        return findById(id);
    }

    public List<PsuFormFactor> getAll() {
        return repository.findAll();
    }

    public Slice<PsuFormFactor> getAll(@ValidPageable final Pageable pageable) {
        return repository.findAllBy(pageable);
    }

    public PsuFormFactor create(@Valid final PsuFormFactor newFormFactor) {
        checkNotExists(null, newFormFactor);
        return repository.save(newFormFactor);
    }

    public void delete(@UUID final String id) {
        repository.deleteById(id);
    }

    public PsuFormFactor update(@UUID final String id, final PsuFormFactor changedFormFactor) {
        checkNotExists(id, changedFormFactor);

        final PsuFormFactor formFactorToBeUpdated = findById(id);
        Optional.ofNullable(changedFormFactor.getName())
                .ifPresent(formFactorToBeUpdated::setName);
        return repository.save(formFactorToBeUpdated);
    }

    public PsuFormFactor replace(@UUID final String id, @Valid final PsuFormFactor newFormFactor) {
        checkNotExists(id, newFormFactor);

        final PsuFormFactor formFactor = findById(id);
        newFormFactor.setId(id);
        newFormFactor.setCreatedAt(formFactor.getCreatedAt());
        return repository.save(newFormFactor);
    }

    private void checkNotExists(final String id, final PsuFormFactor formFactor) {
        repository.findAnotherByName(id, formFactor)
                .ifPresent(entity -> {
                    throw new UniqueNameException(
                            translator.getMessage(
                                    MESSAGE_CODE_PSU_FORM_FACTOR_UNIQUE_NAME,
                                    entity.getName()
                            )
                    );
                });
    }

    private PsuFormFactor findById(final String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_PSU_FORM_FACTOR_NOT_FOUND, id)
                ));
    }
}
