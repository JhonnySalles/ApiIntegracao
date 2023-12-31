package br.com.fenix.apiIntegracao.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class ServerErrorException(message: String? = "Internal server error") : RuntimeException(message) { }