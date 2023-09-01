package br.com.fenix.apiIntegracao.dto.decsubtitle

import br.com.fenix.apiIntegracao.dto.DtoBase
import java.util.*


data class LegendaDto(
    private val id: UUID?,
    var episodio: Int,
    var linguagem: String,
    var tempoInicial: String,
    var tempoFinal: String?,
    var texto: String,
    var traducao: String,
    var vocabulario: String?
) : DtoBase<UUID?>() {

    override fun getId(): UUID? {
        return id
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
