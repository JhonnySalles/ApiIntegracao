package br.com.fenix.apiintegracao.dto.mangaextractor

import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.enums.Linguagens
import br.com.fenix.apiintegracao.model.mangaextractor.MangaPagina
import br.com.fenix.apiintegracao.model.mangaextractor.MangaVocabulario
import java.time.LocalDateTime
import java.util.*

data class MangaCapituloDto(
    private var id: UUID?,
    var manga: String,
    var volume: Int,
    var capitulo: Float,
    var lingua: Linguagens,
    var scan: String,
    var paginas: MutableList<MangaPaginaDto>,
    var isExtra: Boolean,
    var isRaw: Boolean,
    var vocabularios: MutableSet<MangaVocabularioDto>,
    var atualizacao: LocalDateTime?
) : DtoBase<UUID?>() {

    constructor(): this(null, "", 0,0f,Linguagens.PORTUGUESE,"", mutableListOf(),false,false, mutableSetOf(), LocalDateTime.now())

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MangaCapituloDto
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}
