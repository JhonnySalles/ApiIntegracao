package br.com.fenix.apiIntegracao.dto.embedded

interface EmbeddedDtoBase<D> {
    fun getList() : List<D>
}