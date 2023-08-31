package ru.bukhtaev.pcassembler.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"createdAt", "modifiedAt"}, allowGetters = true)
public abstract class BaseDto {

    @Schema(description = "ID")
    protected String id;

    @Schema(description = "Дата и время создания")
    protected Date createdAt;

    @Schema(description = "Дата и время последнего изменения")
    protected Date modifiedAt;
}
