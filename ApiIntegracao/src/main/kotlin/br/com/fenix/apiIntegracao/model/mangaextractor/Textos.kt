package br.com.fenix.apiIntegracao.model.mangaextractor

import java.io.Serializable

data class Textos(
    val id: Long,
    var sequencia: Int,
    var texto: String,
    var posicao_x1: Int,
    var posicao_x2: Int,
    var posicao_y1: Int,
    var posicao_y2: Int,
    var versaoApp: Int
) : Serializable, br.com.fenix.apiIntegracao.model.Entity<Textos, Long> {

    override fun merge(source: Textos) {
        this.sequencia = source.sequencia
        this.texto = source.texto
        this.posicao_x1 = source.posicao_x1
        this.posicao_x2 = source.posicao_x2
        this.posicao_y1 = source.posicao_y1
        this.posicao_y2 = source.posicao_y2
        this.versaoApp = source.versaoApp
    }

    override fun getId(): Long {
        return id
    }

    override fun create(id: Long): Textos {
        return Textos(id, 0, "", 0, 0, 0, 0, 0)
    }

    override fun toString(): String {
        return "MangaTexto [id=$id, texto=$texto, sequencia=$sequencia]"
    }

}
