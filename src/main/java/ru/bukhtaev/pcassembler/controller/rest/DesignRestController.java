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
import ru.bukhtaev.pcassembler.dto.request.DesignRequestDto;
import ru.bukhtaev.pcassembler.dto.response.DesignDto;
import ru.bukhtaev.pcassembler.dto.mapper.DesignMapper;
import ru.bukhtaev.pcassembler.dto.mapper.MapperResolver;
import ru.bukhtaev.pcassembler.model.Design;
import ru.bukhtaev.pcassembler.service.DesignService;
import ru.bukhtaev.pcassembler.util.sort.NameableSort;

import java.util.List;
import java.util.Map;

@Tag(name = "Варианты исполнения")
@RestController
@RequestMapping(DesignRestController.API_V1_DESIGNS)
public class DesignRestController {

    public static final String API_V1_DESIGNS = "/api/v1/designs";

    private final DesignService designService;
    private final DesignMapper mapper;

    @Autowired
    public DesignRestController(
            final DesignService designService,
            final MapperResolver mapperResolver
    ) {
        this.designService = designService;
        this.mapper = mapperResolver.resolve(Design.class);
    }

    @Operation(summary = "Получение всех вариантов исполнения")
    @GetMapping
    public ResponseEntity<List<DesignDto>> handleGetAll() {
        return ResponseEntity.ok(
                designService.getAll()
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @Operation(summary = "Получение всех вариантов исполнения (с пагинацией)")
    @GetMapping("/pageable")
    public ResponseEntity<Slice<DesignDto>> handleGetAll(
            @RequestParam(value = "offset", defaultValue = "0") final Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") final Integer limit,
            @RequestParam(value = "sort", defaultValue = "NAME_ASC") final NameableSort sort
    ) {
        return ResponseEntity.ok(
                designService.getAll(
                        PageRequest.of(offset, limit, sort.getSortValue())
                ).map(mapper::toDto)
        );
    }

    @Operation(summary = "Получение варианта исполнения по ID")
    @GetMapping("/{id}")
    public ResponseEntity<DesignDto> handleGetById(@PathVariable("id") final String id) {
        return ResponseEntity.ok(
                mapper.toDto(designService.getById(id))
        );
    }

    @Operation(summary = "Создание варианта исполнения")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<DesignDto> handleCreate(
            @RequestBody final DesignRequestDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        final DesignDto savedDto = mapper.toDto(
                designService.create(mapper.fromDto(dto))
        );
        return ResponseEntity.created(uriBuilder
                        .path(API_V1_DESIGNS + "/{id}")
                        .build(Map.of("id", savedDto.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedDto);
    }

    @Operation(summary = "Изменение варианта исполнения")
    @PatchMapping("/{id}")
    public ResponseEntity<DesignDto> handleUpdate(
            @PathVariable("id") final String id,
            @RequestBody final DesignRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        designService.update(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Замена варианта исполнения")
    @PutMapping("/{id}")
    public ResponseEntity<DesignDto> handleReplace(
            @PathVariable("id") final String id,
            @RequestBody final DesignRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        designService.replace(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Удаление варианта исполнения по ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleDelete(@PathVariable("id") final String id) {
        designService.delete(id);
    }
}
