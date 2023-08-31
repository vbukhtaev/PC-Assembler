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
import ru.bukhtaev.pcassembler.dto.request.ComputerCaseRequestDto;
import ru.bukhtaev.pcassembler.dto.response.ComputerCaseDto;
import ru.bukhtaev.pcassembler.dto.mapper.ComputerCaseMapper;
import ru.bukhtaev.pcassembler.dto.mapper.MapperResolver;
import ru.bukhtaev.pcassembler.model.ComputerCase;
import ru.bukhtaev.pcassembler.service.ComputerCaseService;
import ru.bukhtaev.pcassembler.util.sort.NameableSort;

import java.util.List;
import java.util.Map;

@Tag(name = "Корпуса")
@RestController
@RequestMapping(ComputerCaseRestController.API_V1_COMPUTER_CASES)
public class ComputerCaseRestController {

    public static final String API_V1_COMPUTER_CASES = "/api/v1/computer-cases";

    private final ComputerCaseService computerCaseService;
    private final ComputerCaseMapper mapper;

    @Autowired
    public ComputerCaseRestController(
            final ComputerCaseService computerCaseService,
            final MapperResolver mapperResolver
    ) {
        this.computerCaseService = computerCaseService;
        this.mapper = mapperResolver.resolve(ComputerCase.class);
    }

    @Operation(summary = "Получение всех корпусов")
    @GetMapping
    public ResponseEntity<List<ComputerCaseDto>> handleGetAll() {
        return ResponseEntity.ok(
                computerCaseService.getAll()
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @Operation(summary = "Получение всех корпусов (с пагинацией)")
    @GetMapping("/pageable")
    public ResponseEntity<Slice<ComputerCaseDto>> handleGetAll(
            @RequestParam(value = "offset", defaultValue = "0") final Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") final Integer limit,
            @RequestParam(value = "sort", defaultValue = "NAME_ASC") final NameableSort sort
    ) {
        return ResponseEntity.ok(
                computerCaseService.getAll(
                        PageRequest.of(offset, limit, sort.getSortValue())
                ).map(mapper::toDto)
        );
    }

    @Operation(summary = "Получение корпуса по ID")
    @GetMapping("/{id}")
    public ResponseEntity<ComputerCaseDto> handleGetById(@PathVariable("id") final String id) {
        return ResponseEntity.ok(
                mapper.toDto(computerCaseService.getById(id))
        );
    }

    @Operation(summary = "Создание корпуса")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<ComputerCaseDto> handleCreate(
            @RequestBody final ComputerCaseRequestDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        final ComputerCaseDto savedDto = mapper.toDto(
                computerCaseService.create(mapper.fromDto(dto))
        );
        return ResponseEntity.created(uriBuilder
                        .path(API_V1_COMPUTER_CASES + "/{id}")
                        .build(Map.of("id", savedDto.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedDto);
    }

    @Operation(summary = "Изменение корпуса")
    @PatchMapping("/{id}")
    public ResponseEntity<ComputerCaseDto> handleUpdate(
            @PathVariable("id") final String id,
            @RequestBody final ComputerCaseRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        computerCaseService.update(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Замена корпуса")
    @PutMapping("/{id}")
    public ResponseEntity<ComputerCaseDto> handleReplace(
            @PathVariable("id") final String id,
            @RequestBody final ComputerCaseRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        computerCaseService.replace(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Удаление корпуса по ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleDelete(@PathVariable("id") final String id) {
        computerCaseService.delete(id);
    }
}
