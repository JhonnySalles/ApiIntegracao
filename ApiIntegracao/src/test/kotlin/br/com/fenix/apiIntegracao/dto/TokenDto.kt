package br.com.fenix.apiIntegracao.dto

import jakarta.xml.bind.annotation.XmlRootElement
import java.io.Serializable
import java.util.*

@XmlRootElement
data class TokenDto(
    val username: String,
    val authenticated: Boolean,
    val created: Date,
    val expiration: Date,
    val accessToken: String,
    val refreshToken: String
) : Serializable
