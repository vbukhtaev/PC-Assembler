package ru.bukhtaev.pcassembler.service.dictionary;

import jakarta.validation.Valid;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.bukhtaev.pcassembler.internationalization.Translator;
import ru.bukhtaev.pcassembler.model.dictionary.PsuCertificate;
import ru.bukhtaev.pcassembler.repository.dictionary.PsuCertificateRepository;
import ru.bukhtaev.pcassembler.util.exception.NotFoundException;
import ru.bukhtaev.pcassembler.util.exception.UniqueNameException;
import ru.bukhtaev.pcassembler.validation.ValidPageable;

import java.util.List;
import java.util.Optional;

import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_PSU_CERTIFICATE_NOT_FOUND;
import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_PSU_CERTIFICATE_UNIQUE_NAME;

@Service
@Validated
public class PsuCertificateService {

    private final Translator translator;
    private final PsuCertificateRepository repository;

    @Autowired
    public PsuCertificateService(
            final Translator translator,
            final PsuCertificateRepository repository
    ) {
        this.translator = translator;
        this.repository = repository;
    }

    public PsuCertificate getById(@UUID final String id) {
        return findById(id);
    }

    public List<PsuCertificate> getAll() {
        return repository.findAll();
    }

    public Slice<PsuCertificate> getAll(@ValidPageable final Pageable pageable) {
        return repository.findAllBy(pageable);
    }

    public PsuCertificate create(@Valid final PsuCertificate newCertificate) {
        checkNotExists(null, newCertificate);
        return repository.save(newCertificate);
    }

    public void delete(@UUID final String id) {
        repository.deleteById(id);
    }

    public PsuCertificate update(@UUID final String id, final PsuCertificate changedCertificate) {
        checkNotExists(id, changedCertificate);

        final PsuCertificate certificateToBeUpdated = findById(id);
        Optional.ofNullable(changedCertificate.getName())
                .ifPresent(certificateToBeUpdated::setName);
        return repository.save(certificateToBeUpdated);
    }

    public PsuCertificate replace(@UUID final String id, @Valid final PsuCertificate newCertificate) {
        checkNotExists(id, newCertificate);

        final PsuCertificate certificate = findById(id);
        newCertificate.setId(id);
        newCertificate.setCreatedAt(certificate.getCreatedAt());
        return repository.save(newCertificate);
    }

    private void checkNotExists(final String id, final PsuCertificate certificate) {
        repository.findAnotherByName(id, certificate)
                .ifPresent(entity -> {
                    throw new UniqueNameException(
                            translator.getMessage(
                                    MESSAGE_CODE_PSU_CERTIFICATE_UNIQUE_NAME,
                                    entity.getName()
                            )
                    );
                });
    }

    private PsuCertificate findById(final String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_PSU_CERTIFICATE_NOT_FOUND, id)
                ));
    }
}
