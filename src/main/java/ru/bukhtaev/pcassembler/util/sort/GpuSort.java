package ru.bukhtaev.pcassembler.util.sort;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum GpuSort {

    ID_ASC(Sort.by(Sort.Direction.ASC, "id")),
    ID_DESC(Sort.by(Sort.Direction.DESC, "id")),
    NAME_ASC(Sort.by(Sort.Direction.ASC, "name")),
    NAME_DESC(Sort.by(Sort.Direction.DESC, "name")),
    MEMORY_SIZE_ASC(Sort.by(Sort.Direction.ASC, "memorySize")),
    MEMORY_SIZE_DESC(Sort.by(Sort.Direction.DESC, "memorySize")),
    CREATED_AT_ASC(Sort.by(Sort.Direction.ASC, "createdAt")),
    CREATED_AT_DESC(Sort.by(Sort.Direction.DESC, "createdAt")),
    MODIFIED_AT_ASC(Sort.by(Sort.Direction.ASC, "modifiedAt")),
    MODIFIED_AT_DESC(Sort.by(Sort.Direction.DESC, "modifiedAt"));

    private final Sort sortValue;
}
