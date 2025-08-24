package br.com.fenix.apiintegracao.dto.mangaextractor

import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.model.mangaextractor.MangaTexto
import br.com.fenix.apiintegracao.model.mangaextractor.MangaVocabulario
import java.time.LocalDateTime
import java.util.*

data class MangaPaginaDto(
    private var id: UUID?,
    var nomePagina: String,
    var numero: Int,
    var hash: String,
    var textos: MutableList<MangaTextoDto>,
    var vocabularios: MutableSet<MangaVocabularioDto>,
    var atualizacao: LocalDateTime?
) : DtoBase<UUID?>() {

    constructor(): this(null,  "", 0,"", mutableListOf(), mutableSetOf(), LocalDateTime.now())

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MangaPaginaDto
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}
