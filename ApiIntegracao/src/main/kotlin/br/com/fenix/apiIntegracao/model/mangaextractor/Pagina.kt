package br.com.fenix.apiIntegracao.model.mangaextractor

import br.com.fenix.apiIntegracao.model.Entity
import java.io.Serializable

data class Pagina(
    private val id: Long?,
    var nome: String,
    var numero: Int,
    var nomePagina: String,
    var hashPagina: String,
    var isProcessado: Boolean,
    var textos: List<Texto> = listOf(),
    var vocabulario: Set<Vocabulario> = setOf()
) : Serializable, Entity<Pagina, Long?> {

    override fun merge(source: Pagina) {
        this.nome = source.nome
        this.numero = source.numero
        this.nomePagina = source.nomePagina
        this.hashPagina = source.hashPagina
        this.isProcessado = source.isProcessado
        this.vocabulario = source.vocabulario
    }

    override fun getId(): Long? {
        return id
    }

    override fun create(id: Long?): Pagina {
        return Pagina(id, "", 0, "", "",false)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Pagina

        if (id != other.id) return false
        if (nome != other.nome) return false
        if (numero != other.numero) return false
        if (hashPagina != other.hashPagina) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + nome.hashCode()
        result = 31 * result + numero
        result = 31 * result + hashPagina.hashCode()
        return result
    }
}
