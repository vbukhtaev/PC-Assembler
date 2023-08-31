package ru.bukhtaev.pcassembler;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "PC Assembler",
                description = "Virtual PC assembling application", version = "1.0.0",
                contact = @Contact(
                        name = "Bukhtaev Vladislav",
                        email = "nonexistentemail@gmail.com",
                        url = "https://t.me/VBukhtaev"
                )
        )
)
public class SwaggerConfig {
}
