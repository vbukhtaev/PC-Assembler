package ru.bukhtaev.pcassembler.util.sort;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum RamModuleSort {

    ID_ASC(Sort.by(Sort.Direction.ASC, "id")),
    ID_DESC(Sort.by(Sort.Direction.DESC, "id")),
    CLOCK_ASC(Sort.by(Sort.Direction.ASC, "clock")),
    CLOCK_DESC(Sort.by(Sort.Direction.DESC, "clock")),
    AMOUNT_ASC(Sort.by(Sort.Direction.ASC, "amount")),
    AMOUNT_DESC(Sort.by(Sort.Direction.DESC, "amount")),
    CREATED_AT_ASC(Sort.by(Sort.Direction.ASC, "createdAt")),
    CREATED_AT_DESC(Sort.by(Sort.Direction.DESC, "createdAt")),
    MODIFIED_AT_ASC(Sort.by(Sort.Direction.ASC, "modifiedAt")),
    MODIFIED_AT_DESC(Sort.by(Sort.Direction.DESC, "modifiedAt"));

    private final Sort sortValue;
}
