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
import ru.bukhtaev.pcassembler.dto.request.PsuRequestDto;
import ru.bukhtaev.pcassembler.dto.response.PsuDto;
import ru.bukhtaev.pcassembler.dto.mapper.MapperResolver;
import ru.bukhtaev.pcassembler.dto.mapper.PsuMapper;
import ru.bukhtaev.pcassembler.model.Psu;
import ru.bukhtaev.pcassembler.service.PsuService;
import ru.bukhtaev.pcassembler.util.sort.NameableSort;

import java.util.List;
import java.util.Map;

@Tag(name = "Блоки питания")
@RestController
@RequestMapping(PsuRestController.API_V1_PSUS)
public class PsuRestController {

    public static final String API_V1_PSUS = "/api/v1/psus";

    private final PsuService psuService;
    private final PsuMapper mapper;

    @Autowired
    public PsuRestController(
            final PsuService psuService,
            final MapperResolver mapperResolver
    ) {
        this.psuService = psuService;
        this.mapper = mapperResolver.resolve(Psu.class);
    }

    @Operation(summary = "Получение всех блоков питания")
    @GetMapping
    public ResponseEntity<List<PsuDto>> handleGetAll() {
        return ResponseEntity.ok(
                psuService.getAll()
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @Operation(summary = "Получение всех блоков питания (с пагинацией)")
    @GetMapping("/pageable")
    public ResponseEntity<Slice<PsuDto>> handleGetAll(
            @RequestParam(value = "offset", defaultValue = "0") final Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") final Integer limit,
            @RequestParam(value = "sort", defaultValue = "NAME_ASC") final NameableSort sort
    ) {
        return ResponseEntity.ok(
                psuService.getAll(
                        PageRequest.of(offset, limit, sort.getSortValue())
                ).map(mapper::toDto)
        );
    }

    @Operation(summary = "Получение блока питания по ID")
    @GetMapping("/{id}")
    public ResponseEntity<PsuDto> handleGetById(@PathVariable("id") final String id) {
        return ResponseEntity.ok(
                mapper.toDto(psuService.getById(id))
        );
    }

    @Operation(summary = "Создание блока питания")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<PsuDto> handleCreate(
            @RequestBody final PsuRequestDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        final PsuDto savedDto = mapper.toDto(
                psuService.create(mapper.fromDto(dto))
        );
        return ResponseEntity.created(uriBuilder
                        .path(API_V1_PSUS + "/{id}")
                        .build(Map.of("id", savedDto.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedDto);
    }

    @Operation(summary = "Изменение блока питания")
    @PatchMapping("/{id}")
    public ResponseEntity<PsuDto> handleUpdate(
            @PathVariable("id") final String id,
            @RequestBody final PsuRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        psuService.update(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Замена блока питания")
    @PutMapping("/{id}")
    public ResponseEntity<PsuDto> handleReplace(
            @PathVariable("id") final String id,
            @RequestBody final PsuRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        psuService.replace(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Удаление блока питания по ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleDelete(@PathVariable("id") final String id) {
        psuService.delete(id);
    }
}
