package br.com.fenix.apiintegracao.model.decksubtitle

import br.com.fenix.apiintegracao.enums.Linguagens
import br.com.fenix.apiintegracao.model.EntityBase
import br.com.fenix.apiintegracao.model.EntityFactory
import java.io.Serializable
import java.util.*

data class Legenda(
    private var id: UUID?,
    var sequencia: Int,
    var episodio: Int,
    var linguagem: Linguagens,
    var tempo: String,
    var texto: String,
    var traducao: String,
    var vocabulario: String
) : Serializable, EntityBase<UUID?, Legenda>() {

    companion object : EntityFactory<UUID?, Legenda> {
        override fun create(id: UUID?): Legenda = Legenda(id, 0, 0, Linguagens.PORTUGUESE, "", "", "", "")
    }

    override fun merge(source: Legenda) {
        this.sequencia = source.sequencia
        this.episodio = source.episodio
        this.tempo = source.tempo
        this.linguagem = source.linguagem
        this.traducao = source.traducao
        this.vocabulario = source.vocabulario
        this.texto = source.texto
    }

    override fun patch(source: Legenda) {
        if (source.sequencia > 0)
            this.sequencia = source.sequencia

        if (source.episodio > 0)
            this.episodio = source.episodio

        if (source.tempo.isNotEmpty())
            this.tempo = source.tempo

        if (source.traducao.isNotEmpty())
            this.traducao = source.traducao

        if (source.vocabulario.isNotEmpty())
            this.vocabulario = source.vocabulario

        if (source.texto.isNotEmpty())
            this.texto = source.texto
    }

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Legenda
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
