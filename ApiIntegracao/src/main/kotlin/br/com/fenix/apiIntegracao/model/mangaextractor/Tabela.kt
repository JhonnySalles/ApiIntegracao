package br.com.fenix.apiIntegracao.model.mangaextractor

data class Tabela(
    private var base: String = "",
    val volumes: List<Volume> = listOf(),
    val quantidade: Int? = null
)