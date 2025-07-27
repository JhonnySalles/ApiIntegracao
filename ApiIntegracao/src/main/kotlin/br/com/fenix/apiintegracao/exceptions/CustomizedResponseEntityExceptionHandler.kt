package br.com.fenix.apiintegracao.exceptions

import com.auth0.jwt.exceptions.TokenExpiredException
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
@RestController
@RestControllerAdvice
class CustomizedResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    companion object {
        private val oLog = LoggerFactory.getLogger(CustomizedResponseEntityExceptionHandler::class.java.name)
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleAllExceptions(ex: Exception, request: HttpServletRequest): ResponseEntity<ExceptionResponse> {
        oLog.error("Internal server error", ex)
        val exceptionResponse = ExceptionResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = ex.message ?: HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
            message = "Ocorreu um erro inesperado no processamento da sua solicitação. Nossa equipe já foi notificada.",
            path = request.requestURI
        )
        return ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(InvalidAuthenticationException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFoundExceptions(ex: Exception, request: HttpServletRequest): ResponseEntity<ExceptionResponse> {
        val exceptionResponse = ExceptionResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = HttpStatus.NOT_FOUND.reasonPhrase,
            message = "O item solicitado não foi encontrado.",
            path = request.requestURI
        )
        return ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(RequiredObjectIsNullException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequestExceptions(ex: Exception, request: HttpServletRequest): ResponseEntity<ExceptionResponse> {
        val exceptionResponse = ExceptionResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = "Solicitação inválida.",
            path = request.requestURI
        )
        return ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidNotFoundException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleInvalidJwtAuthenticationExceptions(ex: Exception, request: HttpServletRequest): ResponseEntity<ExceptionResponse> {
        oLog.warn("Autenticação inválida: ${request.requestURI}. Detalhe: ${ex.message}")
        val exceptionResponse = ExceptionResponse(
            status = HttpStatus.UNAUTHORIZED.value(),
            error = HttpStatus.UNAUTHORIZED.reasonPhrase,
            message = "Necessário estar autenticado no sistema.",
            path = request.requestURI
        )
        return ResponseEntity(exceptionResponse, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(TokenExpiredException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleTokenExpiredException(ex: TokenExpiredException, request: HttpServletRequest): ResponseEntity<ExceptionResponse> {
        oLog.warn("Token expirado para a requisição: ${request.requestURI}. Detalhe: ${ex.message}")
        val errorResponse = ExceptionResponse(
            status = HttpStatus.UNAUTHORIZED.value(),
            error = "Unauthorized",
            message = "Seu token de acesso expirou. Por favor, autentique-se novamente para obter um novo token.",
            path = request.requestURI
        )
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse)
    }

    @ExceptionHandler(ServerErrorException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun serverErrorExceptions(ex: Exception, request: HttpServletRequest): ResponseEntity<ExceptionResponse> {
        oLog.error("Internal server error", ex)
        val exceptionResponse = ExceptionResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = ex.message ?: HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
            message = "Ocorreu um erro inesperado no processamento da sua solicitação. Nossa equipe já foi notificada.",
            path = request.requestURI
        )
        return ResponseEntity(exceptionResponse, HttpStatus.FORBIDDEN)
    }
}