package br.com.fenix.apiintegracao.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class InvalidAuthenticationException(message: String?) : RuntimeException(message) { }