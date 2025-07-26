package br.com.fenix.apiintegracao.dto.mangaextractor

import br.com.fenix.apiintegracao.dto.DtoBase
import java.util.*

data class TextoDto(
    private var id: UUID?,
    var sequencia: Int,
    var texto: String,
    var posicao_x1: Int,
    var posicao_x2: Int,
    var posicao_y1: Int,
    var posicao_y2: Int,
    var versaoApp: Int
) : DtoBase<UUID?>() {

    constructor(): this(null,  0, "",0,0,0,0, 0)

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun toString(): String {
        return "MangaTexto [id=$id, texto=$texto, sequencia=$sequencia]"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TextoDto

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}
