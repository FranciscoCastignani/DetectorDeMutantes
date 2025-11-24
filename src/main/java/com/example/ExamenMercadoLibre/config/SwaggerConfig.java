package com.example.ExamenMercadoLibre.config;

import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenApiCustomizer openAPI() {
        return openApi -> {
            if (openApi.getPaths().containsKey("/mutant")) {
                var operation = openApi.getPaths().get("/mutant").getPost();
                String dnaExample = """
                    {
                      "dna": [
                        "ATGCGA",
                        "CAGTGC",
                        "TTATGT",
                        "AGAAGG",
                        "CCCCTA",
                        "TCACTG"
                      ]
                    }
                    """;
                Example example = new Example().value(dnaExample);
                Content content = operation.getRequestBody().getContent();
                if (content.containsKey("application/json")) {
                    MediaType mediaType = content.get("application/json");
                    mediaType.setExample(example.getValue());
                }
            }
        };
    }
}
