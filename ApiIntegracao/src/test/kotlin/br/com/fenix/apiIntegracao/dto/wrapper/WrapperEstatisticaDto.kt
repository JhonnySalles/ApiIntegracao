package br.com.fenix.apiIntegracao.dto.wrapper

import br.com.fenix.apiIntegracao.dto.textojapones.EstatisticaDto
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class WrapperEstatisticaDto(
    @JsonProperty("_embedded")
    override val embedded: EmbeddedDtoBase<EstatisticaDto>
) : Serializable, WrapperDtoBase<EstatisticaDto> { }
