package ru.bukhtaev.pcassembler.util.sort;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum CpuSort {

    ID_ASC(Sort.by(Sort.Direction.ASC, "id")),
    ID_DESC(Sort.by(Sort.Direction.DESC, "id")),
    NAME_ASC(Sort.by(Sort.Direction.ASC, "name")),
    NAME_DESC(Sort.by(Sort.Direction.DESC, "name")),
    CORE_COUNT_ASC(Sort.by(Sort.Direction.ASC, "coreCount")),
    CORE_COUNT_DESC(Sort.by(Sort.Direction.DESC, "coreCount")),
    THREAD_COUNT_ASC(Sort.by(Sort.Direction.ASC, "threadCount")),
    THREAD_COUNT_DESC(Sort.by(Sort.Direction.DESC, "threadCount")),
    BASE_CLOCK_ASC(Sort.by(Sort.Direction.ASC, "baseClock")),
    BASE_CLOCK_DESC(Sort.by(Sort.Direction.DESC, "baseClock")),
    MAX_CLOCK_ASC(Sort.by(Sort.Direction.ASC, "maxClock")),
    MAX_CLOCK_DESC(Sort.by(Sort.Direction.DESC, "maxClock")),
    L3_CACHE_SIZE_ASC(Sort.by(Sort.Direction.ASC, "l3CacheSize")),
    L3_CACHE_SIZE_DESC(Sort.by(Sort.Direction.DESC, "l3CacheSize")),
    CREATED_AT_ASC(Sort.by(Sort.Direction.ASC, "createdAt")),
    CREATED_AT_DESC(Sort.by(Sort.Direction.DESC, "createdAt")),
    MODIFIED_AT_ASC(Sort.by(Sort.Direction.ASC, "modifiedAt")),
    MODIFIED_AT_DESC(Sort.by(Sort.Direction.DESC, "modifiedAt"));

    private final Sort sortValue;
}
