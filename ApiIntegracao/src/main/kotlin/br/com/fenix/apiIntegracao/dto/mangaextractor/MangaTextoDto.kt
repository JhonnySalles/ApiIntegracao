package br.com.fenix.apiintegracao.dto.mangaextractor

import br.com.fenix.apiintegracao.dto.DtoBase
import java.time.LocalDateTime
import java.util.*

data class MangaTextoDto(
    private var id: UUID?,
    var texto: String,
    var sequencia: Int,
    var x1: Int,
    var y1: Int,
    var x2: Int,
    var y2: Int,
    var atualizacao: LocalDateTime?
) : DtoBase<UUID?>() {

    constructor(): this(null,  "", 0, 0,0,0, 0, LocalDateTime.now())

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MangaTextoDto
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}
