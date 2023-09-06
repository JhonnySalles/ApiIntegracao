package br.com.fenix.apiIntegracao.model.api

import br.com.fenix.apiIntegracao.enums.Conexao
import br.com.fenix.apiIntegracao.enums.Tipo
import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "tabelas")
data class Tabelas(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String,
    @Column
    val url: String,
    @Column
    val porta: String,
    @Column
    var username: String,
    @Column
    var password: String,
    @Column
    var base: String,
    @Column
    var driver: Conexao,
    @Column
    var tipo: Tipo
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tabelas

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}