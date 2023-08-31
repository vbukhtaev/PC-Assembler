package ru.bukhtaev.pcassembler.service.dictionary;

import jakarta.validation.Valid;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.bukhtaev.pcassembler.internationalization.Translator;
import ru.bukhtaev.pcassembler.model.dictionary.FanSize;
import ru.bukhtaev.pcassembler.repository.dictionary.FanSizeRepository;
import ru.bukhtaev.pcassembler.util.exception.NotFoundException;
import ru.bukhtaev.pcassembler.util.exception.UniquePropertyException;
import ru.bukhtaev.pcassembler.validation.ValidPageable;

import java.util.List;
import java.util.Optional;

import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_FAN_SIZE_NOT_FOUND;
import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_FAN_SIZE_UNIQUE_SIZE;

@Service
@Validated
public class FanSizeService {

    private final Translator translator;
    private final FanSizeRepository repository;

    @Autowired
    public FanSizeService(
            final Translator translator,
            final FanSizeRepository repository
    ) {
        this.translator = translator;
        this.repository = repository;
    }

    public FanSize getById(@UUID final String id) {
        return findById(id);
    }

    public List<FanSize> getAll() {
        return repository.findAll();
    }

    public Slice<FanSize> getAll(@ValidPageable final Pageable pageable) {
        return repository.findAllBy(pageable);
    }

    public FanSize create(@Valid final FanSize newFanSize) {
        checkNotExists(null, newFanSize);
        return repository.save(newFanSize);
    }

    public void delete(@UUID final String id) {
        repository.deleteById(id);
    }

    public FanSize update(@UUID final String id, final FanSize changedFanSize) {
        checkNotExists(id, changedFanSize);

        final FanSize fanSizeToBeUpdated = findById(id);
        Optional.ofNullable(changedFanSize.getLength())
                .ifPresent(fanSizeToBeUpdated::setLength);
        Optional.ofNullable(changedFanSize.getWidth())
                .ifPresent(fanSizeToBeUpdated::setWidth);
        Optional.ofNullable(changedFanSize.getHeight())
                .ifPresent(fanSizeToBeUpdated::setHeight);
        return repository.save(fanSizeToBeUpdated);
    }

    public FanSize replace(@UUID final String id, @Valid final FanSize newFanSize) {
        checkNotExists(id, newFanSize);

        final FanSize fanSize = findById(id);
        newFanSize.setId(id);
        newFanSize.setCreatedAt(fanSize.getCreatedAt());
        return repository.save(newFanSize);
    }

    private void checkNotExists(final String id, final FanSize fanSize) {
        repository.findAnotherBySize(id, fanSize)
                .ifPresent(entity -> {
                    throw new UniquePropertyException(
                            translator.getMessage(
                                    MESSAGE_CODE_FAN_SIZE_UNIQUE_SIZE,
                                    entity.getLength(),
                                    entity.getWidth(),
                                    entity.getHeight()
                            ),
                            "length", "width", "height"
                    );
                });
    }

    private FanSize findById(final String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_FAN_SIZE_NOT_FOUND, id)
                ));
    }
}
