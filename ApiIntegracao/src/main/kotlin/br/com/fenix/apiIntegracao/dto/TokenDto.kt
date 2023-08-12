package br.com.fenix.apiIntegracao.dto

import java.io.Serializable
import java.util.*

data class TokenDto(
    private val username: String,
    private val authenticated: Boolean,
    private val created: Date,
    private val expiration: Date,
    private val accessToken: String,
    private val refreshToken: String,
) : Serializable { }