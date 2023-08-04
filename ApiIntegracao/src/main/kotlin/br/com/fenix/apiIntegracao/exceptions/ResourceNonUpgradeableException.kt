package br.com.fenix.apiIntegracao.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_MODIFIED)
class ResourceNonUpgradeableException(message: String?) : RuntimeException(message) { }