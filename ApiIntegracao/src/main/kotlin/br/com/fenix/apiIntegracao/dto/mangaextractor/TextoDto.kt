package br.com.fenix.apiIntegracao.dto.mangaextractor

import br.com.fenix.apiIntegracao.dto.DtoBase
import java.util.*

data class TextoDto(
    private val id: UUID?,
    var sequencia: Int,
    var texto: String,
    var posicao_x1: Int,
    var posicao_x2: Int,
    var posicao_y1: Int,
    var posicao_y2: Int,
    var versaoApp: Int
) : DtoBase<UUID?>() {

    override fun getId(): UUID? {
        return id
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
