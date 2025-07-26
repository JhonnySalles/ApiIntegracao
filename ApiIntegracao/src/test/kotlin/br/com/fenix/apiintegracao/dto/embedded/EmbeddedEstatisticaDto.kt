package br.com.fenix.apiintegracao.dto.embedded

import br.com.fenix.apiintegracao.dto.textojapones.EstatisticaDto
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class EmbeddedEstatisticaDto(
    @JsonProperty("estatisticaList")
    val estatisticas: List<EstatisticaDto>
) : Serializable, EmbeddedDtoBase<EstatisticaDto> {

    override fun getList(): List<EstatisticaDto> = estatisticas

}