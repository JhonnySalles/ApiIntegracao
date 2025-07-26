package br.com.fenix.apiintegracao.dto.wrapper

import br.com.fenix.apiintegracao.dto.embedded.EmbeddedDtoBase
import br.com.fenix.apiintegracao.dto.textojapones.EstatisticaDto
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class WrapperEstatisticaDto(
    @JsonProperty("_embedded")
    override val embedded: EmbeddedDtoBase<EstatisticaDto>
) : Serializable, WrapperDtoBase<EstatisticaDto> { }
