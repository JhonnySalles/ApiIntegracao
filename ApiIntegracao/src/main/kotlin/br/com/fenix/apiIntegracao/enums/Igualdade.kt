package br.com.fenix.apiintegracao.enums

enum class Igualdade(val valor : String) {
    IGUAL("="),
    MENOR("<"),
    MAIOR(">"),
    MENOR_IGUAL("<="),
    MAIOR_IGUAL(">="),
    DIFERENTE("!=");
}