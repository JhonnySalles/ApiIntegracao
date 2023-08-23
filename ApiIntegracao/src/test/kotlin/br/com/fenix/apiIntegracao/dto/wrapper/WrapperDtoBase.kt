package br.com.fenix.apiIntegracao.dto.wrapper

interface WrapperDtoBase<D> {
    val embedded : EmbeddedDtoBase<D>
}