package br.com.fenix.apiintegracao.dto.wrapper

import br.com.fenix.apiintegracao.dto.embedded.EmbeddedDtoBase

interface WrapperDtoBase<D> {
    val embedded : EmbeddedDtoBase<D>
}