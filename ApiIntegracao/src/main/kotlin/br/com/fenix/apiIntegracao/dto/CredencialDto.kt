package br.com.fenix.apiIntegracao.dto

import java.io.Serializable

data class CredencialDto(
    val username: String?,
    val password: String?
) : Serializable