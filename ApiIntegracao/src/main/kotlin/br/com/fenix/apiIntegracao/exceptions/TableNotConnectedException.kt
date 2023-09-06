package br.com.fenix.apiIntegracao.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class TableNotConnectedException(message: String? = "Table not connected in server") : RuntimeException(message) { }