package br.com.fenix.apiintegracao.dto.api

import java.io.Serializable

data class CredencialDto(
    val username: String?,
    val password: String?
) : Serializable {
    constructor(): this("", "")
}