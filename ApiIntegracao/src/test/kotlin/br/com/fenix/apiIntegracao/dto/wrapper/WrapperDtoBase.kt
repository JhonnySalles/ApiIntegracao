package br.com.fenix.apiIntegracao.dto.wrapper

import br.com.fenix.apiIntegracao.dto.embedded.EmbeddedDtoBase

interface WrapperDtoBase<D> {
    val embedded : EmbeddedDtoBase<D>
}