package br.com.fenix.apiintegracao.dto.embedded

interface EmbeddedDtoBase<D> {
    fun getList() : List<D>
}