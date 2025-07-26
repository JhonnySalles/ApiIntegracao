package br.com.fenix.apiintegracao.dto

import jakarta.xml.bind.annotation.XmlRootElement
import java.io.Serializable

@XmlRootElement
data class AccountCredentialDto(
    var username: String,
    val password: String
) : Serializable
