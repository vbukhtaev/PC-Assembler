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
import ru.bukhtaev.pcassembler.dto.request.GraphicsCardRequestDto;
import ru.bukhtaev.pcassembler.dto.response.GraphicsCardDto;
import ru.bukhtaev.pcassembler.dto.mapper.GraphicsCardMapper;
import ru.bukhtaev.pcassembler.dto.mapper.MapperResolver;
import ru.bukhtaev.pcassembler.model.GraphicsCard;
import ru.bukhtaev.pcassembler.service.GraphicsCardService;
import ru.bukhtaev.pcassembler.util.sort.NameableSort;

import java.util.List;
import java.util.Map;

@Tag(name = "Видеокарты")
@RestController
@RequestMapping(GraphicsCardRestController.API_V1_GRAPHICS_CARDS)
public class GraphicsCardRestController {

    public static final String API_V1_GRAPHICS_CARDS = "/api/v1/graphics-cards";

    private final GraphicsCardService graphicsCardService;
    private final GraphicsCardMapper mapper;

    @Autowired
    public GraphicsCardRestController(
            final GraphicsCardService graphicsCardService,
            final MapperResolver mapperResolver
    ) {
        this.graphicsCardService = graphicsCardService;
        this.mapper = mapperResolver.resolve(GraphicsCard.class);
    }

    @Operation(summary = "Получение всех видеокарт")
    @GetMapping
    public ResponseEntity<List<GraphicsCardDto>> handleGetAll() {
        return ResponseEntity.ok(
                graphicsCardService.getAll()
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @Operation(summary = "Получение всех видеокарт (с пагинацией)")
    @GetMapping("/pageable")
    public ResponseEntity<Slice<GraphicsCardDto>> handleGetAll(
            @RequestParam(value = "offset", defaultValue = "0") final Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") final Integer limit,
            @RequestParam(value = "sort", defaultValue = "NAME_ASC") final NameableSort sort
    ) {
        return ResponseEntity.ok(
                graphicsCardService.getAll(
                        PageRequest.of(offset, limit, sort.getSortValue())
                ).map(mapper::toDto)
        );
    }

    @Operation(summary = "Получение видеокарты по ID")
    @GetMapping("/{id}")
    public ResponseEntity<GraphicsCardDto> handleGetById(@PathVariable("id") final String id) {
        return ResponseEntity.ok(
                mapper.toDto(graphicsCardService.getById(id))
        );
    }

    @Operation(summary = "Создание видеокарты")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<GraphicsCardDto> handleCreate(
            @RequestBody final GraphicsCardRequestDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        final GraphicsCardDto savedDto = mapper.toDto(
                graphicsCardService.create(mapper.fromDto(dto))
        );
        return ResponseEntity.created(uriBuilder
                        .path(API_V1_GRAPHICS_CARDS + "/{id}")
                        .build(Map.of("id", savedDto.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedDto);
    }

    @Operation(summary = "Изменение видеокарты")
    @PatchMapping("/{id}")
    public ResponseEntity<GraphicsCardDto> handleUpdate(
            @PathVariable("id") final String id,
            @RequestBody final GraphicsCardRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        graphicsCardService.update(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Замена видеокарты")
    @PutMapping("/{id}")
    public ResponseEntity<GraphicsCardDto> handleReplace(
            @PathVariable("id") final String id,
            @RequestBody final GraphicsCardRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        graphicsCardService.replace(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Удаление видеокарты по ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleDelete(@PathVariable("id") final String id) {
        graphicsCardService.delete(id);
    }
}
