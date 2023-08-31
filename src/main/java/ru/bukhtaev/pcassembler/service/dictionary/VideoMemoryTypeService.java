package ru.bukhtaev.pcassembler.service.dictionary;

import jakarta.validation.Valid;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.bukhtaev.pcassembler.internationalization.Translator;
import ru.bukhtaev.pcassembler.model.dictionary.VideoMemoryType;
import ru.bukhtaev.pcassembler.repository.dictionary.VideoMemoryTypeRepository;
import ru.bukhtaev.pcassembler.util.exception.NotFoundException;
import ru.bukhtaev.pcassembler.util.exception.UniqueNameException;
import ru.bukhtaev.pcassembler.validation.ValidPageable;

import java.util.List;
import java.util.Optional;

import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_VIDEO_MEMORY_TYPE_NOT_FOUND;
import static ru.bukhtaev.pcassembler.internationalization.MessageCodeUtils.MESSAGE_CODE_VIDEO_MEMORY_TYPE_UNIQUE_NAME;

@Service
@Validated
public class VideoMemoryTypeService {

    private final Translator translator;
    private final VideoMemoryTypeRepository repository;

    @Autowired
    public VideoMemoryTypeService(
            final Translator translator,
            final VideoMemoryTypeRepository repository
    ) {
        this.translator = translator;
        this.repository = repository;
    }

    public VideoMemoryType getById(@UUID final String id) {
        return findById(id);
    }

    public List<VideoMemoryType> getAll() {
        return repository.findAll();
    }

    public Slice<VideoMemoryType> getAll(@ValidPageable final Pageable pageable) {
        return repository.findAllBy(pageable);
    }

    public VideoMemoryType create(@Valid final VideoMemoryType newMemoryType) {
        checkNotExists(null, newMemoryType);
        return repository.save(newMemoryType);
    }

    public void delete(@UUID final String id) {
        repository.deleteById(id);
    }

    public VideoMemoryType update(@UUID final String id, final VideoMemoryType changedMemoryType) {
        checkNotExists(id, changedMemoryType);

        final VideoMemoryType memoryType = findById(id);
        Optional.ofNullable(changedMemoryType.getName())
                .ifPresent(memoryType::setName);
        return repository.save(memoryType);
    }

    public VideoMemoryType replace(@UUID final String id, @Valid final VideoMemoryType newMemoryType) {
        checkNotExists(id, newMemoryType);

        final VideoMemoryType memoryType = findById(id);
        newMemoryType.setId(id);
        newMemoryType.setCreatedAt(memoryType.getCreatedAt());
        return repository.save(newMemoryType);
    }

    private void checkNotExists(final String id, final VideoMemoryType memoryType) {
        repository.findAnotherByName(id, memoryType)
                .ifPresent(entity -> {
                    throw new UniqueNameException(
                            translator.getMessage(
                                    MESSAGE_CODE_VIDEO_MEMORY_TYPE_UNIQUE_NAME,
                                    entity.getName()
                            )
                    );
                });
    }

    private VideoMemoryType findById(final String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        translator.getMessage(MESSAGE_CODE_VIDEO_MEMORY_TYPE_NOT_FOUND, id)
                ));
    }
}
