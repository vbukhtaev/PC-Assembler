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
import ru.bukhtaev.pcassembler.model.Design;
import ru.bukhtaev.pcassembler.model.dictionary.Vendor;
import ru.bukhtaev.pcassembler.repository.DesignRepository;
import ru.bukhtaev.pcassembler.repository.dictionary.VendorRepository;
import ru.bukhtaev.pcassembler.util.exception.InvalidPropertyException;
import ru.bukhtaev.pcassembler.util.exception.NotFoundException;
import ru.bukhtaev.pcassembler.util.exception.UniqueNameException;
import ru.bukhtaev.pcassembler.validation.ValidPageable;

import java.util.List;
import java.util.Optional;

import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.*;

@Service
@Validated
public class DesignService {

    private final Translator translator;
    private final DesignRepository designRepository;
    private final VendorRepository vendorRepository;

    @Autowired
    public DesignService(
            final Translator translator,
            final DesignRepository designRepository,
            final VendorRepository vendorRepository
    ) {
        this.translator = translator;
        this.designRepository = designRepository;
        this.vendorRepository = vendorRepository;
    }

    public Design getById(@UUID final String id) {
        return findById(id);
    }

    public List<Design> getAll() {
        return designRepository.findAll();
    }

    public Slice<Design> getAll(@ValidPageable final Pageable pageable) {
        return designRepository.findAllBy(pageable);
    }

    public Design create(@Valid final Design newDesign) {
        setRelatedEntities(newDesign);
        checkNotExists(null, newDesign);

        return designRepository.save(newDesign);
    }

    public void delete(@UUID final String id) {
        designRepository.deleteById(id);
    }

    public Design update(@UUID final String id, final Design changedDesign) {
        final Design designToBeUpdated = findById(id);
        updateChangedProperties(designToBeUpdated, changedDesign);
        checkNotExists(id, designToBeUpdated);

        return designRepository.save(designToBeUpdated);
    }

    public Design replace(@UUID final String id, @Valid final Design newDesign) {
        setRelatedEntities(newDesign);
        checkNotExists(id, newDesign);

        final Design design = findById(id);
        newDesign.setId(id);
        newDesign.setCreatedAt(design.getCreatedAt());
        return designRepository.save(newDesign);
    }

    private void updateChangedProperties(final Design designToBeUpdated, final Design changedDesign) {
        Optional.ofNullable(changedDesign.getName()).ifPresent(designToBeUpdated::setName);

        final Vendor newVendor = changedDesign.getVendor();
        if (newVendor != null && newVendor.getId() != null) {
            final Vendor vendor = vendorRepository.findById(newVendor.getId())
                    .orElseThrow(() -> new NotFoundException(
                            translator.getMessage(MESSAGE_CODE_VENDOR_NOT_FOUND, newVendor.getId())
                    ));
            designToBeUpdated.setVendor(vendor);
        }
    }

    private void setRelatedEntities(final Design design) {
        final Vendor newVendor = design.getVendor();
        if (newVendor == null || newVendor.getId() == null) {
            throw new InvalidPropertyException(
                    translator.getMessage(MessageCodeUtils.MESSAGE_CODE_INVALID_FIELD),
                    "vendor"
            );
        }

        final Vendor foundVendor = vendorRepository.findById(newVendor.getId())
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_VENDOR_NOT_FOUND, newVendor.getId())
                ));
        design.setVendor(foundVendor);
    }

    private void checkNotExists(final String id, final Design design) {
        designRepository.findAnotherByName(id, design)
                .ifPresent(entity -> {
                    throw new UniqueNameException(
                            translator.getMessage(
                                    MESSAGE_CODE_DESIGN_UNIQUE_NAME,
                                    entity.getName()
                            )
                    );
                });
    }

    private Design findById(final String id) {
        return designRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_DESIGN_NOT_FOUND, id)
                ));
    }
}
