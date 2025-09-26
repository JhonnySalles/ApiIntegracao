package br.com.fenix.apiintegracao.dto.mangaextractor

import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.enums.Linguagens
import br.com.fenix.apiintegracao.views.Views
import com.fasterxml.jackson.annotation.JsonView
import java.time.LocalDateTime
import java.util.*

data class MangaCapituloDto(
    @JsonView(Views.Summary::class)
    private var id: UUID?,
    @JsonView(Views.Detail::class)
    var manga: String,
    @JsonView(Views.Detail::class)
    var volume: Int,
    @JsonView(Views.Detail::class)
    var capitulo: Float,
    @JsonView(Views.Detail::class)
    var lingua: Linguagens,
    @JsonView(Views.Detail::class)
    var scan: String,
    @JsonView(Views.Summary::class)
    var paginas: MutableList<MangaPaginaDto>,
    @JsonView(Views.Detail::class)
    var isExtra: Boolean,
    @JsonView(Views.Detail::class)
    var isRaw: Boolean,
    @JsonView(Views.Summary::class)
    var vocabularios: MutableSet<MangaVocabularioDto>,
    @JsonView(Views.Summary::class)
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
