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
import ru.bukhtaev.pcassembler.dto.request.RamModuleRequestDto;
import ru.bukhtaev.pcassembler.dto.response.RamModuleDto;
import ru.bukhtaev.pcassembler.dto.mapper.MapperResolver;
import ru.bukhtaev.pcassembler.dto.mapper.RamModuleMapper;
import ru.bukhtaev.pcassembler.model.RamModule;
import ru.bukhtaev.pcassembler.service.RamModuleService;
import ru.bukhtaev.pcassembler.util.sort.RamModuleSort;

import java.util.List;
import java.util.Map;

@Tag(name = "Оперативная память")
@RestController
@RequestMapping(RamModuleRestController.API_V1_RAM_MODULES)
public class RamModuleRestController {

    public static final String API_V1_RAM_MODULES = "/api/v1/ram-modules";

    private final RamModuleService ramModuleService;
    private final RamModuleMapper mapper;

    @Autowired
    public RamModuleRestController(
            final RamModuleService ramModuleService,
            final MapperResolver mapperResolver
    ) {
        this.ramModuleService = ramModuleService;
        this.mapper = mapperResolver.resolve(RamModule.class);
    }

    @Operation(summary = "Получение всех модулей оперативной памяти")
    @GetMapping
    public ResponseEntity<List<RamModuleDto>> handleGetAll() {
        return ResponseEntity.ok(
                ramModuleService.getAll()
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @Operation(summary = "Получение всех модулей оперативной памяти (с пагинацией)")
    @GetMapping("/pageable")
    public ResponseEntity<Slice<RamModuleDto>> handleGetAll(
            @RequestParam(value = "offset", defaultValue = "0") final Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") final Integer limit,
            @RequestParam(value = "sort", defaultValue = "NAME_ASC") final RamModuleSort sort
    ) {
        return ResponseEntity.ok(
                ramModuleService.getAll(
                        PageRequest.of(offset, limit, sort.getSortValue())
                ).map(mapper::toDto)
        );
    }

    @Operation(summary = "Получение модуля оперативной памяти по ID")
    @GetMapping("/{id}")
    public ResponseEntity<RamModuleDto> handleGetById(@PathVariable("id") final String id) {
        return ResponseEntity.ok(
                mapper.toDto(ramModuleService.getById(id))
        );
    }

    @Operation(summary = "Создание модуля оперативной памяти")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<RamModuleDto> handleCreate(
            @RequestBody final RamModuleRequestDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        final RamModuleDto savedDto = mapper.toDto(
                ramModuleService.create(mapper.fromDto(dto))
        );
        return ResponseEntity.created(uriBuilder
                        .path(API_V1_RAM_MODULES + "/{id}")
                        .build(Map.of("id", savedDto.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedDto);
    }

    @Operation(summary = "Изменение модуля оперативной памяти")
    @PatchMapping("/{id}")
    public ResponseEntity<RamModuleDto> handleUpdate(
            @PathVariable("id") final String id,
            @RequestBody final RamModuleRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        ramModuleService.update(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Замена модуля оперативной памяти")
    @PutMapping("/{id}")
    public ResponseEntity<RamModuleDto> handleReplace(
            @PathVariable("id") final String id,
            @RequestBody final RamModuleRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        ramModuleService.replace(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Удаление модуля оперативной памяти по ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleDelete(@PathVariable("id") final String id) {
        ramModuleService.delete(id);
    }
}
