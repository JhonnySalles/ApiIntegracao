package br.com.fenix.apiIntegracao.dto.api

import java.io.Serializable
import java.util.*

data class TokenDto(
    val username: String,
    val authenticated: Boolean,
    val created: Date,
    val expiration: Date,
    val accessToken: String,
    val refreshToken: String,
) : Serializable {

    constructor(): this("", true, Date(),Date(),"","")

}