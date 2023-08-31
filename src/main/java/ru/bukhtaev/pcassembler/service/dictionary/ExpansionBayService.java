package ru.bukhtaev.pcassembler.service.dictionary;

import jakarta.validation.Valid;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.bukhtaev.pcassembler.internationalization.Translator;
import ru.bukhtaev.pcassembler.model.dictionary.ExpansionBay;
import ru.bukhtaev.pcassembler.repository.dictionary.ExpansionBayRepository;
import ru.bukhtaev.pcassembler.util.exception.NotFoundException;
import ru.bukhtaev.pcassembler.util.exception.UniquePropertyException;
import ru.bukhtaev.pcassembler.validation.ValidPageable;

import java.util.List;
import java.util.Optional;

import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_EXPANSION_BAY_NOT_FOUND;
import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_EXPANSION_BAY_UNIQUE_SIZE;

@Service
@Validated
public class ExpansionBayService {

    private final Translator translator;
    private final ExpansionBayRepository repository;

    @Autowired
    public ExpansionBayService(
            final Translator translator,
            final ExpansionBayRepository repository
    ) {
        this.translator = translator;
        this.repository = repository;
    }

    public ExpansionBay getById(@UUID final String id) {
        return findById(id);
    }

    public List<ExpansionBay> getAll() {
        return repository.findAll();
    }

    public Slice<ExpansionBay> getAll(@ValidPageable final Pageable pageable) {
        return repository.findAllBy(pageable);
    }

    public ExpansionBay create(@Valid final ExpansionBay newExpansionBay) {
        checkNotExists(null, newExpansionBay);
        return repository.save(newExpansionBay);
    }

    public void delete(@UUID final String id) {
        repository.deleteById(id);
    }

    public ExpansionBay update(@UUID final String id, final ExpansionBay changedExpansionBay) {
        checkNotExists(id, changedExpansionBay);

        final ExpansionBay expansionBayToBeUpdated = findById(id);
        Optional.ofNullable(changedExpansionBay.getSize())
                .ifPresent(expansionBayToBeUpdated::setSize);
        return repository.save(expansionBayToBeUpdated);
    }

    public ExpansionBay replace(@UUID final String id, @Valid final ExpansionBay newExpansionBay) {
        checkNotExists(id, newExpansionBay);

        final ExpansionBay expansionBay = findById(id);
        newExpansionBay.setId(id);
        newExpansionBay.setCreatedAt(expansionBay.getCreatedAt());
        return repository.save(newExpansionBay);
    }

    private void checkNotExists(final String id, final ExpansionBay expansionBay) {
        repository.findAnotherBySize(id, expansionBay)
                .ifPresent(entity -> {
                    throw new UniquePropertyException(
                            translator.getMessage(
                                    MESSAGE_CODE_EXPANSION_BAY_UNIQUE_SIZE,
                                    entity.getSize()
                            ),
                            "size"
                    );
                });
    }


    private ExpansionBay findById(final String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_EXPANSION_BAY_NOT_FOUND, id)
                ));
    }
}
