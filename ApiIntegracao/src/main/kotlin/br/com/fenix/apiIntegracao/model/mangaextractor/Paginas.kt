package br.com.fenix.apiIntegracao.model.mangaextractor

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.io.Serializable

data class Paginas(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    var nome: String,
    var numero: Int,
    var hashPagina: String,
    var isProcessado: Boolean,
    var vocabulario: String,
    var textos: List<Textos> = listOf()
) : Serializable, br.com.fenix.apiIntegracao.model.Entity<Paginas, Long> {

    override fun merge(source: Paginas) {
        this.nome = source.nome
        this.numero = source.numero
        this.hashPagina = source.hashPagina
        this.isProcessado = source.isProcessado
        this.vocabulario = source.vocabulario
    }

    override fun getId(): Long {
        return id
    }

    override fun create(id: Long): Paginas {
        return Paginas(id, "", 0, "", false, "", listOf())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Paginas

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
