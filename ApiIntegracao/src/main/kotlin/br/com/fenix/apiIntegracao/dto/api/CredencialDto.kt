package br.com.fenix.apiIntegracao.dto.api

import java.io.Serializable

data class CredencialDto(
    val username: String?,
    val password: String?
) : Serializable