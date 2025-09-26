package br.com.fenix.apiintegracao.dto.novelextractor

import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.views.Views
import com.fasterxml.jackson.annotation.JsonFilter
import com.fasterxml.jackson.annotation.JsonView
import java.time.LocalDateTime
import java.util.*

data class NovelTextoDto(
    @JsonView(Views.Summary::class)
    private var id: UUID?,
    @JsonView(Views.Detail::class)
    var texto: String,
    @JsonView(Views.Detail::class)
    var sequencia: Int,
    @JsonView(Views.Summary::class)
    var atualizacao: LocalDateTime?
) : DtoBase<UUID?>() {

    constructor(): this(null,  "", 0, LocalDateTime.now())

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as NovelTextoDto
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}
