package br.com.fenix.apiIntegracao.dto.mangaextractor

import br.com.fenix.apiIntegracao.dto.DtoBase
import java.util.*

data class PaginaDto(
    private val id: UUID?,
    var nome: String,
    var numero: Int,
    var nomePagina: String,
    var hashPagina: String,
    var isProcessado: Boolean,
    var textos: List<TextoDto> = listOf(),
    var vocabulario: Set<VocabularioDto> = setOf()
) : DtoBase<UUID?>() {

    constructor(): this(null,  "", 0,"","",false)

    override fun getId(): UUID? {
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PaginaDto

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}
