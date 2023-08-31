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
import ru.bukhtaev.pcassembler.dto.request.MotherboardRequestDto;
import ru.bukhtaev.pcassembler.dto.response.MotherboardDto;
import ru.bukhtaev.pcassembler.dto.mapper.MapperResolver;
import ru.bukhtaev.pcassembler.dto.mapper.MotherboardMapper;
import ru.bukhtaev.pcassembler.model.Motherboard;
import ru.bukhtaev.pcassembler.service.MotherboardService;
import ru.bukhtaev.pcassembler.util.sort.NameableSort;

import java.util.List;
import java.util.Map;

@Tag(name = "Материнские платы")
@RestController
@RequestMapping(MotherboardRestController.API_V1_MOTHERBOARDS)
public class MotherboardRestController {

    public static final String API_V1_MOTHERBOARDS = "/api/v1/motherboards";

    private final MotherboardService motherboardService;
    private final MotherboardMapper mapper;

    @Autowired
    public MotherboardRestController(
            final MotherboardService motherboardService,
            final MapperResolver mapperResolver
    ) {
        this.motherboardService = motherboardService;
        this.mapper = mapperResolver.resolve(Motherboard.class);
    }

    @Operation(summary = "Получение всех материнских плат")
    @GetMapping
    public ResponseEntity<List<MotherboardDto>> handleGetAll() {
        return ResponseEntity.ok(
                motherboardService.getAll()
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @Operation(summary = "Получение всех материнских плат (с пагинацией)")
    @GetMapping("/pageable")
    public ResponseEntity<Slice<MotherboardDto>> handleGetAll(
            @RequestParam(value = "offset", defaultValue = "0") final Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") final Integer limit,
            @RequestParam(value = "sort", defaultValue = "NAME_ASC") final NameableSort sort
    ) {
        return ResponseEntity.ok(
                motherboardService.getAll(
                        PageRequest.of(offset, limit, sort.getSortValue())
                ).map(mapper::toDto)
        );
    }

    @Operation(summary = "Получение материнской платы по ID")
    @GetMapping("/{id}")
    public ResponseEntity<MotherboardDto> handleGetById(@PathVariable("id") final String id) {
        return ResponseEntity.ok(
                mapper.toDto(motherboardService.getById(id))
        );
    }

    @Operation(summary = "Создание материнской платы")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<MotherboardDto> handleCreate(
            @RequestBody final MotherboardRequestDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        final MotherboardDto savedDto = mapper.toDto(
                motherboardService.create(mapper.fromDto(dto))
        );
        return ResponseEntity.created(uriBuilder
                        .path(API_V1_MOTHERBOARDS + "/{id}")
                        .build(Map.of("id", savedDto.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedDto);
    }

    @Operation(summary = "Изменение материнской платы")
    @PatchMapping("/{id}")
    public ResponseEntity<MotherboardDto> handleUpdate(
            @PathVariable("id") final String id,
            @RequestBody final MotherboardRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        motherboardService.update(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Замена материнской платы")
    @PutMapping("/{id}")
    public ResponseEntity<MotherboardDto> handleReplace(
            @PathVariable("id") final String id,
            @RequestBody final MotherboardRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        motherboardService.replace(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Удаление материнской платы по ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleDelete(@PathVariable("id") final String id) {
        motherboardService.delete(id);
    }
}
