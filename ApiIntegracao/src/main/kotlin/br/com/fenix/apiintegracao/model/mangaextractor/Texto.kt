package br.com.fenix.apiintegracao.model.mangaextractor

import br.com.fenix.apiintegracao.model.EntityBase
import java.io.Serializable
import java.util.*

data class Texto(
    private var id: UUID?,
    var sequencia: Int,
    var texto: String,
    var posicao_x1: Int,
    var posicao_x2: Int,
    var posicao_y1: Int,
    var posicao_y2: Int,
    var versaoApp: Int
) : Serializable, EntityBase<Texto, UUID?>() {

    override fun merge(source: Texto) {
        this.sequencia = source.sequencia
        this.texto = source.texto
        this.posicao_x1 = source.posicao_x1
        this.posicao_x2 = source.posicao_x2
        this.posicao_y1 = source.posicao_y1
        this.posicao_y2 = source.posicao_y2
        this.versaoApp = source.versaoApp
    }

    override fun patch(source: Texto) {
        TODO("Not yet implemented")
    }

    override fun getId(): UUID? {
        return id
    }

    fun setId(id: UUID?) {
        this.id = id;
    }

    override fun create(id: UUID?): Texto {
        return Texto(id, 0, "", 0, 0, 0, 0, 0)
    }

    override fun toString(): String {
        return "MangaTexto [id=$id, texto=$texto, sequencia=$sequencia]"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Texto

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}
