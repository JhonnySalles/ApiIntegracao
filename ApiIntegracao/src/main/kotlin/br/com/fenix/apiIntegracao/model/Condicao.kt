package br.com.fenix.apiintegracao.model

import br.com.fenix.apiintegracao.enums.Igualdade

data class Condicao(
    val valor: Any,
    val igualdade: Igualdade? = null
)