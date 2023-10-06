package br.com.fenix.apiIntegracao.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

@ControllerAdvice
@RestController
class CustomizedResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    val LOG = Logger.getLogger(CustomizedResponseEntityExceptionHandler::class.java.name)

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(
        ex: Exception, request: WebRequest
    ): ResponseEntity<ExceptionResponse> {
        LOG.log(Level.SEVERE, "Internal server error", ex)
        val exceptionResponse = ExceptionResponse(
            Date(),
            ex.message,
            request.getDescription(false)
        )
        return ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(InvalidAuthenticationException::class)
    fun handleNotFoundExceptions(
        ex: Exception, request: WebRequest
    ): ResponseEntity<ExceptionResponse> {
        val exceptionResponse = ExceptionResponse(
            Date(),
            ex.message,
            request.getDescription(false)
        )
        return ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(RequiredObjectIsNullException::class)
    fun handleBadRequestExceptions(
        ex: Exception, request: WebRequest
    ): ResponseEntity<ExceptionResponse> {
        val exceptionResponse = ExceptionResponse(
            Date(),
            ex.message,
            request.getDescription(false)
        )
        return ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidNotFoundException::class)
    fun handleInvalidJwtAuthenticationExceptions(
        ex: Exception, request: WebRequest
    ): ResponseEntity<ExceptionResponse> {
        val exceptionResponse = ExceptionResponse(
            Date(),
            ex.message,
            request.getDescription(false)
        )
        return ResponseEntity(exceptionResponse, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(ServerErrorException::class)
    fun serverErrorExceptions(
        ex: Exception, request: WebRequest
    ): ResponseEntity<ExceptionResponse> {
        val exceptionResponse = ExceptionResponse(
            Date(),
            ex.message,
            request.getDescription(false)
        )
        return ResponseEntity(exceptionResponse, HttpStatus.FORBIDDEN)
    }
}