package ru.bukhtaev.pcassembler.util.sort;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum FanSizeSort {

    ID_ASC(Sort.by(Sort.Direction.ASC, "id")),
    ID_DESC(Sort.by(Sort.Direction.DESC, "id")),
    CREATED_AT_ASC(Sort.by(Sort.Direction.ASC, "createdAt")),
    CREATED_AT_DESC(Sort.by(Sort.Direction.DESC, "createdAt")),
    MODIFIED_AT_ASC(Sort.by(Sort.Direction.ASC, "modifiedAt")),
    MODIFIED_AT_DESC(Sort.by(Sort.Direction.DESC, "modifiedAt")),
    LENGTH_ASC(Sort.by(Sort.Direction.ASC, "length")),
    LENGTH_DESC(Sort.by(Sort.Direction.DESC, "length")),
    WIDTH_ASC(Sort.by(Sort.Direction.ASC, "width")),
    WIDTH_DESC(Sort.by(Sort.Direction.DESC, "width")),
    HEIGHT_ASC(Sort.by(Sort.Direction.ASC, "height")),
    HEIGHT_DESC(Sort.by(Sort.Direction.DESC, "height"));

    private final Sort sortValue;
}
