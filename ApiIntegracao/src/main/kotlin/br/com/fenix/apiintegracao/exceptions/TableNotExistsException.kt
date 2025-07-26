package br.com.fenix.apiintegracao.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class TableNotExistsException(message: String? = "Table not exists in server") : RuntimeException(message) { }