package br.com.fenix.apiintegracao.dto.mangaextractor

import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.enums.Linguagens
import br.com.fenix.apiintegracao.views.Views
import com.fasterxml.jackson.annotation.JsonView
import java.time.LocalDateTime
import java.util.*

data class MangaVolumeDto(
    @JsonView(Views.Summary::class)
    private var id: UUID?,
    @JsonView(Views.Detail::class)
    var manga: String,
    @JsonView(Views.Detail::class)
    var volume: Int,
    @JsonView(Views.Detail::class)
    var lingua: Linguagens,
    @JsonView(Views.Summary::class)
    var capitulos: MutableList<MangaCapituloDto>,
    @JsonView(Views.Summary::class)
    var vocabularios: MutableSet<MangaVocabularioDto>,
    @JsonView(Views.Detail::class)
    var arquivo: String,
    @JsonView(Views.Detail::class)
    var processado: Boolean,
    @JsonView(Views.Summary::class)
    var capa: MangaCapaDto?,
    @JsonView(Views.Summary::class)
    var atualizacao: LocalDateTime?
) : DtoBase<UUID?>() {

    constructor(): this(null,  "", 0, Linguagens.PORTUGUESE, mutableListOf(), mutableSetOf(),"", false, null, LocalDateTime.now())

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MangaVolumeDto

        if (manga != other.manga) return false
        if (volume != other.volume) return false
        if (lingua != other.lingua) return false

        return true
    }

    override fun hashCode(): Int {
        var result = manga.hashCode()
        result = 31 * result + volume
        result = 31 * result + lingua.hashCode()
        return result
    }

}
