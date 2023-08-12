package br.com.fenix.apiIntegracao.dto

import java.io.Serializable

data class CredencialDto(
    private var username: String,
    private var password: String
) : Serializable { }