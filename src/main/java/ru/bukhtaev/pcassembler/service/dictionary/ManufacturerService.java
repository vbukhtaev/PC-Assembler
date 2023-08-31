package ru.bukhtaev.pcassembler.service.dictionary;

import jakarta.validation.Valid;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.bukhtaev.pcassembler.internationalization.Translator;
import ru.bukhtaev.pcassembler.model.dictionary.Manufacturer;
import ru.bukhtaev.pcassembler.repository.dictionary.ManufacturerRepository;
import ru.bukhtaev.pcassembler.util.exception.NotFoundException;
import ru.bukhtaev.pcassembler.util.exception.UniqueNameException;
import ru.bukhtaev.pcassembler.validation.ValidPageable;

import java.util.List;
import java.util.Optional;

import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_MANUFACTURER_NOT_FOUND;
import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_MANUFACTURER_UNIQUE_NAME;

@Service
@Validated
public class ManufacturerService {

    private final Translator translator;
    private final ManufacturerRepository repository;

    @Autowired
    public ManufacturerService(
            final Translator translator,
            final ManufacturerRepository repository
    ) {
        this.translator = translator;
        this.repository = repository;
    }

    public Manufacturer getById(@UUID final String id) {
        return findById(id);
    }

    public List<Manufacturer> getAll() {
        return repository.findAll();
    }

    public Slice<Manufacturer> getAll(@ValidPageable final Pageable pageable) {
        return repository.findAllBy(pageable);
    }

    public Manufacturer create(@Valid final Manufacturer newManufacturer) {
        checkNotExists(null, newManufacturer);
        return repository.save(newManufacturer);
    }

    public void delete(@UUID final String id) {
        repository.deleteById(id);
    }

    public Manufacturer update(@UUID final String id, final Manufacturer changedManufacturer) {
        checkNotExists(id, changedManufacturer);

        final Manufacturer manufacturerToBeUpdated = findById(id);
        Optional.ofNullable(changedManufacturer.getName())
                .ifPresent(manufacturerToBeUpdated::setName);
        return repository.save(manufacturerToBeUpdated);
    }

    public Manufacturer replace(@UUID final String id, @Valid final Manufacturer newManufacturer) {
        checkNotExists(id, newManufacturer);

        final Manufacturer manufacturer = findById(id);
        newManufacturer.setId(id);
        newManufacturer.setCreatedAt(manufacturer.getCreatedAt());
        return repository.save(newManufacturer);
    }

    private void checkNotExists(final String id, final Manufacturer manufacturer) {
        repository.findAnotherByName(id, manufacturer)
                .ifPresent(entity -> {
                    throw new UniqueNameException(
                            translator.getMessage(
                                    MESSAGE_CODE_MANUFACTURER_UNIQUE_NAME,
                                    entity.getName()
                            )
                    );
                });
    }

    private Manufacturer findById(final String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_MANUFACTURER_NOT_FOUND, id)
                ));
    }
}
