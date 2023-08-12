package br.com.fenix.apiIntegracao.dto

import java.io.Serializable
import java.time.LocalDateTime

data class AtualizacaoDto(
    val id: String,
    val computador: String,
    var ip: String,
    var UltimaConsulta: LocalDateTime
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AtualizacaoDto

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}