package ru.bukhtaev.pcassembler.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.bukhtaev.pcassembler.dto.request.HddRequestDto;
import ru.bukhtaev.pcassembler.dto.response.HddDto;
import ru.bukhtaev.pcassembler.dto.mapper.HddMapper;
import ru.bukhtaev.pcassembler.dto.mapper.MapperResolver;
import ru.bukhtaev.pcassembler.model.Hdd;
import ru.bukhtaev.pcassembler.service.HddService;
import ru.bukhtaev.pcassembler.util.sort.NameableSort;

import java.util.List;
import java.util.Map;

@Tag(name = "HDD")
@RestController
@RequestMapping(HddRestController.API_V1_HDDS)
public class HddRestController {

    public static final String API_V1_HDDS = "/api/v1/hdds";

    private final HddService hddService;
    private final HddMapper mapper;

    @Autowired
    public HddRestController(
            final HddService hddService,
            final MapperResolver mapperResolver
    ) {
        this.hddService = hddService;
        this.mapper = mapperResolver.resolve(Hdd.class);
    }

    @Operation(summary = "Получение всех HDD")
    @GetMapping
    public ResponseEntity<List<HddDto>> handleGetAll() {
        return ResponseEntity.ok(
                hddService.getAll()
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @Operation(summary = "Получение всех HDD (с пагинацией)")
    @GetMapping("/pageable")
    public ResponseEntity<Slice<HddDto>> handleGetAll(
            @RequestParam(value = "offset", defaultValue = "0") final Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") final Integer limit,
            @RequestParam(value = "sort", defaultValue = "NAME_ASC") final NameableSort sort
    ) {
        return ResponseEntity.ok(
                hddService.getAll(
                        PageRequest.of(offset, limit, sort.getSortValue())
                ).map(mapper::toDto)
        );
    }

    @Operation(summary = "Получение HDD по ID")
    @GetMapping("/{id}")
    public ResponseEntity<HddDto> handleGetById(@PathVariable("id") final String id) {
        return ResponseEntity.ok(
                mapper.toDto(hddService.getById(id))
        );
    }

    @Operation(summary = "Создание HDD")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<HddDto> handleCreate(
            @RequestBody final HddRequestDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        final HddDto savedDto = mapper.toDto(
                hddService.create(mapper.fromDto(dto))
        );
        return ResponseEntity.created(uriBuilder
                        .path(API_V1_HDDS + "/{id}")
                        .build(Map.of("id", savedDto.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedDto);
    }

    @Operation(summary = "Изменение HDD")
    @PatchMapping("/{id}")
    public ResponseEntity<HddDto> handleUpdate(
            @PathVariable("id") final String id,
            @RequestBody final HddRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        hddService.update(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Замена HDD")
    @PutMapping("/{id}")
    public ResponseEntity<HddDto> handleReplace(
            @PathVariable("id") final String id,
            @RequestBody final HddRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        hddService.replace(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Удаление HDD по ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleDelete(@PathVariable("id") final String id) {
        hddService.delete(id);
    }
}
