package ru.bukhtaev.pcassembler.controller.rest.dictionary;

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
import ru.bukhtaev.pcassembler.dto.dictionary.NameableDto;
import ru.bukhtaev.pcassembler.dto.mapper.MapperResolver;
import ru.bukhtaev.pcassembler.dto.dictionary.mapper.VideoMemoryTypeMapper;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableRequestDto;
import ru.bukhtaev.pcassembler.model.dictionary.VideoMemoryType;
import ru.bukhtaev.pcassembler.service.dictionary.VideoMemoryTypeService;
import ru.bukhtaev.pcassembler.util.sort.NameableSort;

import java.util.List;
import java.util.Map;

@Tag(name = "Типы видеопамяти")
@RestController
@RequestMapping(VideoMemoryTypeRestController.API_V1_VIDEO_MEMORY_TYPES)
public class VideoMemoryTypeRestController {

    public static final String API_V1_VIDEO_MEMORY_TYPES = "/api/v1/video-memory-types";

    private final VideoMemoryTypeService videoMemoryTypeService;
    private final VideoMemoryTypeMapper mapper;

    @Autowired
    public VideoMemoryTypeRestController(
            final VideoMemoryTypeService videoMemoryTypeService,
            final MapperResolver mapperResolver
    ) {
        this.videoMemoryTypeService = videoMemoryTypeService;
        this.mapper = mapperResolver.resolve(VideoMemoryType.class);
    }

    @Operation(summary = "Получение всех типов видеопамяти")
    @GetMapping
    public ResponseEntity<List<NameableDto>> handleGetAll() {
        return ResponseEntity.ok(
                videoMemoryTypeService.getAll()
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @Operation(summary = "Получение всех типов видеопамяти (с пагинацией)")
    @GetMapping("/pageable")
    public ResponseEntity<Slice<NameableDto>> handleGetAll(
            @RequestParam(value = "offset", defaultValue = "0") final Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") final Integer limit,
            @RequestParam(value = "sort", defaultValue = "NAME_ASC") final NameableSort sort
    ) {
        return ResponseEntity.ok(
                videoMemoryTypeService.getAll(
                        PageRequest.of(offset, limit, sort.getSortValue())
                ).map(mapper::toDto)
        );
    }

    @Operation(summary = "Получение типа видеопамяти по ID")
    @GetMapping("/{id}")
    public ResponseEntity<NameableDto> handleGetById(@PathVariable("id") final String id) {
        return ResponseEntity.ok(
                mapper.toDto(videoMemoryTypeService.getById(id))
        );
    }

    @Operation(summary = "Создание типа видеопамяти")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<NameableDto> handleCreate(
            @RequestBody final NameableRequestDto dto,
            UriComponentsBuilder uriBuilder
    ) {
        final NameableDto savedDto = mapper.toDto(
                videoMemoryTypeService.create(mapper.fromDto(dto))
        );
        return ResponseEntity.created(uriBuilder
                        .path(API_V1_VIDEO_MEMORY_TYPES + "/{id}")
                        .build(Map.of("id", savedDto.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedDto);
    }

    @Operation(summary = "Изменение типа видеопамяти")
    @PatchMapping("/{id}")
    public ResponseEntity<NameableDto> handleUpdate(
            @PathVariable("id") final String id,
            @RequestBody final NameableRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        videoMemoryTypeService.update(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Замена типа видеопамяти")
    @PutMapping("/{id}")
    public ResponseEntity<NameableDto> handleReplace(
            @PathVariable("id") final String id,
            @RequestBody final NameableRequestDto dto
    ) {
        return ResponseEntity.ok(
                mapper.toDto(
                        videoMemoryTypeService.replace(id, mapper.fromDto(dto))
                )
        );
    }

    @Operation(summary = "Удаление типа видеопамяти по ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleDelete(@PathVariable("id") final String id) {
        videoMemoryTypeService.delete(id);
    }
}
