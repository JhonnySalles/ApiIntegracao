package br.com.fenix.apiintegracao.dto.mangaextractor

import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.enums.Linguagens
import java.util.*

data class CapituloDto(
    private val id: UUID?,
    var manga: String,
    var volume: Int,
    var capitulo: Double,
    var linguagem: Linguagens?,
    var scan: String,
    var isExtra: Boolean,
    var isRaw: Boolean,
    var isProcessado: Boolean,
    var paginas: List<PaginaDto> = listOf(),
    var vocabulario: Set<VocabularioDto> = setOf()
) : DtoBase<UUID?>() {

    constructor(): this(null, "", 0,0.0,null,"",false,false,false)

    override fun getId(): UUID? {
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CapituloDto

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}
