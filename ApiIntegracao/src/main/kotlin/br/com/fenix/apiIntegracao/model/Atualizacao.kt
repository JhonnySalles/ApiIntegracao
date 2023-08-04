package br.com.fenix.apiIntegracao.model

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.io.Serializable
import java.time.LocalDateTime

data class Atualizacao(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String,
    @Column(name = "Computador", length = 250, nullable = false)
    val computador: String,
    @Column(name = "Ip", length = 250, nullable = false)
    var ip: String,
    @Column(nullable = false)
    var UltimaConsulta: LocalDateTime
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Atualizacao

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}