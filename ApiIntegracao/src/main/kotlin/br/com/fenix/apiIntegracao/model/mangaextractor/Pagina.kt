package br.com.fenix.apiIntegracao.model.mangaextractor

import br.com.fenix.apiIntegracao.model.Entity
import java.io.Serializable
import java.util.*

data class Pagina(
    private val id: UUID?,
    var nome: String,
    var numero: Int,
    var nomePagina: String,
    var hashPagina: String,
    var isProcessado: Boolean,
    var textos: List<Texto> = listOf(),
    var vocabulario: Set<Vocabulario> = setOf()
) : Serializable, Entity<Pagina, UUID?> {

    override fun merge(source: Pagina) {
        this.nome = source.nome
        this.numero = source.numero
        this.nomePagina = source.nomePagina
        this.hashPagina = source.hashPagina
        this.isProcessado = source.isProcessado
        this.vocabulario = source.vocabulario
    }

    override fun getId(): UUID? {
        return id
    }

    override fun create(id: UUID?): Pagina {
        return Pagina(id, "", 0, "", "",false)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Pagina

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}
