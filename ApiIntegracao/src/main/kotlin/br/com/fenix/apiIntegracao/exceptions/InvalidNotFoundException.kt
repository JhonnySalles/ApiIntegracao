package br.com.fenix.apiIntegracao.exceptions

import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class InvalidNotFoundException(message: String?) : AuthenticationException(message) { }