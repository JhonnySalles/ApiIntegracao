package br.com.fenix.apiIntegracao.model.mangaextractor

data class Tabelas(
    var base: String = "",
    val volumes: List<Volumes> = listOf(),
    val quantidade: Int? = null
)