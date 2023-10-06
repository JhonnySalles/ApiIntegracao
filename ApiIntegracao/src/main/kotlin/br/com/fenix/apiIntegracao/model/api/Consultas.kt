package br.com.fenix.apiIntegracao.model.api

import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(name = "consultas")
data class Consultas(
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

        other as Consultas

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}