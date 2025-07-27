package br.com.fenix.apiintegracao.exceptions

import com.auth0.jwt.exceptions.JWTVerificationException
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.time.LocalDateTime

@Component
class FilterExceptionHandler : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            // Tenta continuar a execução normal da cadeia de filtros
            filterChain.doFilter(request, response)
        } catch (ex: JWTVerificationException) {
            // Captura exceções específicas de validação do JWT (TokenExpiredException é uma subclasse)
            log.warn("Erro de validação de token JWT interceptado pelo filtro: ${ex.message}")
            setErrorResponse(HttpStatus.UNAUTHORIZED, request, response, "Token de acesso inválido, ausente ou expirado.")
        } catch (ex: Exception) {
            // Captura qualquer outro erro inesperado que possa ocorrer nos filtros
            log.error("Erro inesperado na cadeia de filtros interceptado: ${request.requestURI}", ex)
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, request, response, "Ocorreu um erro inesperado no processamento da sua solicitação.")
        }
    }

    private fun setErrorResponse(status: HttpStatus, request: HttpServletRequest, response: HttpServletResponse, message: String) {
        response.status = status.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        val errorResponse = mapOf(
            "timestamp" to LocalDateTime.now().toString(),
            "status" to status.value(),
            "error" to status.reasonPhrase,
            "message" to message,
            "path" to request.requestURI
        )

        try {
            val objectMapper = ObjectMapper()
            // Garantir que o ObjectMapper consiga serializar LocalDateTime
            objectMapper.findAndRegisterModules()
            response.writer.write(objectMapper.writeValueAsString(errorResponse))
        } catch (e: Exception) {
            log.error("Erro ao escrever a resposta JSON de erro.", e)
        }
    }
}