package br.com.fenix.apiintegracao.dto.novelextractor

import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.enums.Linguagens
import br.com.fenix.apiintegracao.views.Views
import com.fasterxml.jackson.annotation.JsonFilter
import com.fasterxml.jackson.annotation.JsonView
import java.time.LocalDateTime
import java.util.*

data class NovelVolumeDto(
    @JsonView(Views.Summary::class)
    private var id: UUID?,
    @JsonView(Views.Detail::class)
    var novel: String,
    @JsonView(Views.Detail::class)
    var titulo: String,
    @JsonView(Views.Detail::class)
    var tituloAlternativo: String,
    @JsonView(Views.Detail::class)
    var serie: String,
    @JsonView(Views.Detail::class)
    var descricao: String,
    @JsonView(Views.Detail::class)
    var arquivo: String,
    @JsonView(Views.Detail::class)
    var editora: String,
    @JsonView(Views.Detail::class)
    var autor: String,
    @JsonView(Views.Detail::class)
    var volume: Float,
    @JsonView(Views.Detail::class)
    var lingua: Linguagens,
    @JsonView(Views.Detail::class)
    var favorito: Boolean,
    @JsonView(Views.Detail::class)
    var processado: Boolean,
    @JsonView(Views.Summary::class)
    var capa: NovelCapaDto?,
    @JsonView(Views.Summary::class)
    var capitulos: MutableList<NovelCapituloDto>,
    @JsonView(Views.Summary::class)
    var vocabularios: MutableSet<NovelVocabularioDto>,
    @JsonView(Views.Summary::class)
    var atualizacao: LocalDateTime?
) : DtoBase<UUID?>() {

    constructor(): this(null,  "", "","","","","","","",0f, Linguagens.PORTUGUESE, false, false, null, mutableListOf(), mutableSetOf(), LocalDateTime.now())

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NovelVolumeDto

        if (novel != other.novel) return false
        if (volume != other.volume) return false
        if (lingua != other.lingua) return false

        return true
    }

    override fun hashCode(): Int {
        var result = novel.hashCode()
        result = 31 * result + volume.hashCode()
        result = 31 * result + lingua.hashCode()
        return result
    }

}
