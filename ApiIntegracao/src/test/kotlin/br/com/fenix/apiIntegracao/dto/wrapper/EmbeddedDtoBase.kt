package br.com.fenix.apiIntegracao.dto.wrapper

interface EmbeddedDtoBase<D> {
    fun getList() : List<D>
}