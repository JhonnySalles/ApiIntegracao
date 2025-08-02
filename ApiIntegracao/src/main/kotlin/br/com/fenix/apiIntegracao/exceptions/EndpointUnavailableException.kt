package br.com.fenix.apiintegracao.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class EndpointUnavailableException(message: String? = "Enpoint unavailable for a object") : RuntimeException(message) { }