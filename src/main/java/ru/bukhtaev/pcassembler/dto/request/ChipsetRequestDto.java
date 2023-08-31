package ru.bukhtaev.pcassembler.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.UUID;
import ru.bukhtaev.pcassembler.dto.dictionary.NameableRequestDto;

@Schema(description = "Чипсет")
@Getter
public class ChipsetRequestDto extends NameableRequestDto {

    @UUID
    @NotBlank
    protected String socketId;

    @Builder
    public ChipsetRequestDto(final String name, final String socketId) {
        super(name);
        this.socketId = socketId;
    }
}
