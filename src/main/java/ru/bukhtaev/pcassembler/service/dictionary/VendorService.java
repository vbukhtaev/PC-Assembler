package ru.bukhtaev.pcassembler.service.dictionary;

import jakarta.validation.Valid;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.bukhtaev.pcassembler.internationalization.Translator;
import ru.bukhtaev.pcassembler.model.dictionary.Vendor;
import ru.bukhtaev.pcassembler.repository.dictionary.VendorRepository;
import ru.bukhtaev.pcassembler.util.exception.NotFoundException;
import ru.bukhtaev.pcassembler.util.exception.UniqueNameException;
import ru.bukhtaev.pcassembler.validation.ValidPageable;

import java.util.List;
import java.util.Optional;

import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_VENDOR_NOT_FOUND;
import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_VENDOR_UNIQUE_NAME;

@Service
@Validated
public class VendorService {

    private final Translator translator;
    private final VendorRepository repository;

    @Autowired
    public VendorService(
            final Translator translator,
            final VendorRepository repository
    ) {
        this.translator = translator;
        this.repository = repository;
    }

    public Vendor getById(@UUID final String id) {
        return findById(id);
    }

    public List<Vendor> getAll() {
        return repository.findAll();
    }

    public Slice<Vendor> getAll(@ValidPageable final Pageable pageable) {
        return repository.findAllBy(pageable);
    }

    public Vendor create(@Valid final Vendor newVendor) {
        checkNotExists(null, newVendor);
        return repository.save(newVendor);
    }

    public void delete(@UUID final String id) {
        repository.deleteById(id);
    }

    public Vendor update(@UUID final String id, final Vendor changedVendor) {
        checkNotExists(id, changedVendor);

        final Vendor vendorToBeUpdated = findById(id);
        Optional.ofNullable(changedVendor.getName())
                .ifPresent(vendorToBeUpdated::setName);
        return repository.save(vendorToBeUpdated);
    }

    public Vendor replace(@UUID final String id, @Valid final Vendor newVendor) {
        checkNotExists(id, newVendor);

        final Vendor vendor = findById(id);
        newVendor.setId(id);
        newVendor.setCreatedAt(vendor.getCreatedAt());
        return repository.save(newVendor);
    }

    private void checkNotExists(final String id, final Vendor vendor) {
        repository.findAnotherByName(id, vendor)
                .ifPresent(entity -> {
                    throw new UniqueNameException(
                            translator.getMessage(
                                    MESSAGE_CODE_VENDOR_UNIQUE_NAME,
                                    entity.getName()
                            )
                    );
                });
    }

    private Vendor findById(final String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_VENDOR_NOT_FOUND, id)
                ));
    }
}
