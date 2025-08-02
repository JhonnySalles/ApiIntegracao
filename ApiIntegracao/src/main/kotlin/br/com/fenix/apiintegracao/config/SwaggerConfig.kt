package br.com.fenix.apiintegracao.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun customOpenApi(): OpenAPI {
        return OpenAPI().info(
            Info().title("Api Integração").version("v1").description("").termsOfService("").license(
                License().name("Apache 2.0").url("")
            )
        )
    }
}