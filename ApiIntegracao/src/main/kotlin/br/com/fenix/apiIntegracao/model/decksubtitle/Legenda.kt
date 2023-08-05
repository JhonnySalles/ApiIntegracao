package br.com.fenix.apiIntegracao.model.decksubtitle

import br.com.fenix.apiIntegracao.model.Entity
import java.io.Serializable
import java.util.*


data class Legenda(
    private val id: UUID?,
    var episodio: Int,
    var linguagem: String,
    var tempoInicial: String,
    var tempoFinal: String?,
    var texto: String,
    var traducao: String,
    var vocabulario: String?
) : Serializable, Entity<Legenda, UUID?> {

    override fun merge(source: Legenda) {
        this.episodio = source.episodio
        this.tempoInicial = source.tempoInicial
        this.tempoFinal = source.tempoFinal
        this.linguagem = source.linguagem
        this.traducao = source.traducao
        this.vocabulario = source.vocabulario
        this.texto = source.texto
    }

    override fun getId(): UUID? {
        return id
    }

    override fun create(id: UUID?): Legenda {
        return Legenda(id, 0, "", "", null, "", "", null)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Legenda

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
