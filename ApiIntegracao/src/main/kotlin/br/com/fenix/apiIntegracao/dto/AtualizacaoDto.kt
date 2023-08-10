package br.com.fenix.apiIntegracao.dto

import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(name = "atualizacoes")
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