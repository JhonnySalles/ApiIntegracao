package br.com.fenix.apiintegracao.dto.decsubtitle

import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.views.Views
import com.fasterxml.jackson.annotation.JsonView
import java.util.*

data class LegendaDto(
    @JsonView(Views.Summary::class)
    private var id: UUID?,
    @JsonView(Views.Detail::class)
    var episodio: Int,
    @JsonView(Views.Detail::class)
    var linguagem: String,
    @JsonView(Views.Detail::class)
    var tempoInicial: String,
    @JsonView(Views.Detail::class)
    var tempoFinal: String?,
    @JsonView(Views.Detail::class)
    var texto: String,
    @JsonView(Views.Detail::class)
    var traducao: String,
    @JsonView(Views.Detail::class)
    var vocabulario: String?
) : DtoBase<UUID?>() {

    constructor(): this(null, 0, "","","","","","")

    override fun getId(): UUID? {
        return id
    }

    override fun setId(id: UUID?) {
        this.id = id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LegendaDto

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
