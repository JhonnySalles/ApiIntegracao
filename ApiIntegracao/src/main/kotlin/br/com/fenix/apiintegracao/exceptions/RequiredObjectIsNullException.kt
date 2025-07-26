package br.com.fenix.apiintegracao.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class RequiredObjectIsNullException(message: String? = "Its required inform a object") : RuntimeException(message) { }