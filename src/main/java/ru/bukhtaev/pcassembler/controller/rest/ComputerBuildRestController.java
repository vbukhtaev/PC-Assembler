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
import ru.bukhtaev.pcassembler.dto.request.ComputerBuildRequestDto;
import ru.bukhtaev.pcassembler.dto.response.ComputerBuildDto;
import ru.bukhtaev.pcassembler.dto.mapper.MapperResolver;
import ru.bukhtaev.pcassembler.dto.mapper.ComputerBuildMapper;
import ru.bukhtaev.pcassembler.model.ComputerBuild;
import ru.bukhtaev.pcassembler.service.ComputerBuildService;
import ru.bukhtaev.pcassembler.util.sort.NameableSort;

import java.util.List;
import java.util.Map;

@Tag(name = "Сборки ПК")
@RestController
@RequestMapping(ComputerBuildRestController.API_V1_COMPUTER_BUILDS)
public class ComputerBuildRestController {

    public static final String API_V1_COMPUTER_BUILDS = "/api/v1/computer-builds";

    private final ComputerBuildService computerBuildService;
    private final ComputerBuildMapper mapper;

    @Autowired
    public ComputerBuildRestController(
            final ComputerBuildService computerBuildService,
            final MapperResolver mapperResolver
    ) {
        this.computerBuildService = computerBuildService;
        this.mapper = mapperResolver.resolve(ComputerBuild.class);
    }

    @Operation(summary = "Получение всех сборок ПК")
    @GetMapping
    public ResponseEntity<List<ComputerBuildDto>> handleGetAll() {
        return ResponseEntity.ok(
                computerBuildService.getAll()
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @Operation(summary = "Получение всех сборок ПК (с пагинацией)")
    @GetMapping("/pageable")
    public ResponseEntity<Slice<ComputerBuildDto>> handleGetAll(
            @RequestParam(value = "offset", defaultValue = "0") final Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") final Integer limit,
            @RequestParam(value = "sort", defaultValue = "NAME_ASC") final NameableSort sort
    ) {
        return ResponseEntity.ok(
                computerBuildService.getAll(
                        PageRequest.of(offset, limit, sort.getSortValue())
                ).map(mapper::toDto)
        );
    }

    @Operation(summary = "Получение сборки ПК по ID")
    @GetMapping("/{id}")
    public ResponseEntity<ComputerBuildDto> handleGetById(@PathVariable("id") final String id) {
        return ResponseEntity.ok(
                mapper.toDto(computerBuildService.getById(id))
        );
    }

    @Operation(summary = "Создание сборки ПК")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<ComputerBuildDto> handleCreate(
            @RequestBody final ComputerBuildRequestDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        final ComputerBuildDto savedDto = mapper.toDto(
                computerBuildService.create(mapper.fromDto(dto))
        );
        return ResponseEntity.created(uriBuilder
                        .path(API_V1_COMPUTER_BUILDS + "/{id}")
                        .build(Map.of("id", savedDto.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedDto);
    }

    @Operation(summary = "Изменение сборки ПК")
    @PatchMapping("/{id}")
    public ResponseEntity<ComputerBuildDto> handleUpdate(
            @PathVariable("id") final String id,
            @RequestBody final ComputerBuildRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        computerBuildService.update(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Замена сборки ПК")
    @PutMapping("/{id}")
    public ResponseEntity<ComputerBuildDto> handleReplace(
            @PathVariable("id") final String id,
            @RequestBody final ComputerBuildRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        computerBuildService.replace(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Удаление сборки ПК по ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleDelete(@PathVariable("id") final String id) {
        computerBuildService.delete(id);
    }
}
