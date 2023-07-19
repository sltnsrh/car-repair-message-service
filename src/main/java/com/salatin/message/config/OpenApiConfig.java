package com.salatin.message.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = "OpenApi specification for Message Service",
        contact = @Contact(
            name = "Serhii Salatin",
            url = "https://github.com/sltnsrh"
        ),
        version = "0.0.1"
    )
)
public class OpenApiConfig {
}
