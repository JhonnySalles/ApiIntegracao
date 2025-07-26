package br.com.fenix.apiintegracao.exceptions

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*

@ControllerAdvice
@RestController
class CustomizedResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    companion object {
        private val oLog = LoggerFactory.getLogger(CustomizedResponseEntityExceptionHandler::class.java.name)
    }

    @ExceptionHandler(Exception::class)
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
    fun handleInvalidJwtAuthenticationExceptions(ex: Exception, request: HttpServletRequest): ResponseEntity<ExceptionResponse> {
        val exceptionResponse = ExceptionResponse(
            status = HttpStatus.UNAUTHORIZED.value(),
            error = HttpStatus.UNAUTHORIZED.reasonPhrase,
            message = "Necessário estar autenticado no sistema.",
            path = request.requestURI
        )
        return ResponseEntity(exceptionResponse, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(ServerErrorException::class)
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