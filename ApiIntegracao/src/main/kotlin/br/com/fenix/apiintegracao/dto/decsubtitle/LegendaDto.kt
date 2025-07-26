package br.com.fenix.apiintegracao.dto.decsubtitle

import br.com.fenix.apiintegracao.dto.DtoBase
import java.util.*


data class LegendaDto(
    private var id: UUID?,
    var episodio: Int,
    var linguagem: String,
    var tempoInicial: String,
    var tempoFinal: String?,
    var texto: String,
    var traducao: String,
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
