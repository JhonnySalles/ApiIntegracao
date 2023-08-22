package br.com.fenix.apiIntegracao.dto.wrapper

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class WrapperEstatisticaDto(
    @JsonProperty("_embedded")
    var embedded: EmbeddedEstatisticaDto
) : Serializable
