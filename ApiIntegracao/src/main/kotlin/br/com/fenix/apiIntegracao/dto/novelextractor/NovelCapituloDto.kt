package br.com.fenix.apiintegracao.dto.novelextractor

import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.enums.Linguagens
import br.com.fenix.apiintegracao.views.Views
import com.fasterxml.jackson.annotation.JsonFilter
import com.fasterxml.jackson.annotation.JsonView
import java.time.LocalDateTime
import java.util.*

data class NovelCapituloDto(
    @JsonView(Views.Summary::class)
    private var id: UUID?,
    @JsonView(Views.Detail::class)
    var novel: String,
    @JsonView(Views.Detail::class)
    var volume: Float,
    @JsonView(Views.Detail::class)
    var capitulo: Float,
    @JsonView(Views.Detail::class)
    var descricao: String,
    @JsonView(Views.Detail::class)
    var sequencia: Int,
    @JsonView(Views.Detail::class)
    var lingua: Linguagens,
    @JsonView(Views.Summary::class)
    var textos: MutableList<NovelTextoDto>,
    @JsonView(Views.Summary::class)
    var vocabularios: MutableSet<NovelVocabularioDto>,
    @JsonView(Views.Summary::class)
    var atualizacao: LocalDateTime?
) : DtoBase<UUID?>() {

    constructor(): this(null, "", 0f,0f,"",0, Linguagens.PORTUGUESE, mutableListOf(), mutableSetOf(), LocalDateTime.now())

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as NovelCapituloDto
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}
