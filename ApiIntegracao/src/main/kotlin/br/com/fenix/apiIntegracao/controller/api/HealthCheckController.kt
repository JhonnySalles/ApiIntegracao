package br.com.fenix.apiintegracao.controller.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
@Tag(name = "Healthcheck", description = "Verificação de status da API")
class HealthCheckController {

    @Operation(
        summary = "Rota de verificação de status da API",
        description = """Endpoint de verificação de status (Health Check). É utilizado para monitoramento e para confirmar que a API está em execução e respondendo a requisições.

**Parâmetros:**
* Nenhum.

**Corpo da Resposta:**
* Em caso de sucesso (`200 OK`), retorna uma mensagem de texto simples confirmando que o serviço está operacional."""
    )
    @ApiResponse(
        responseCode = "200",
        description = "Sucesso. A API está online e respondendo.",
        content = [
            Content(
                mediaType = MediaType.TEXT_PLAIN_VALUE,
                schema = Schema(type = "string", example = "A API está funcionando! Acesse /swagger-ui para ver a documentação.")
            )
        ]
    )
    @GetMapping(value = ["/health"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun healthCheck(): ResponseEntity<String> {
        return ResponseEntity.ok("A API está funcionando! Acesse /swagger-ui para ver a documentação, ou /v3/api-docs para o json.")
    }

}