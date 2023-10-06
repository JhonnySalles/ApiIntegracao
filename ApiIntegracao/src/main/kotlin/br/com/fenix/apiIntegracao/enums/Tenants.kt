package br.com.fenix.apiIntegracao.enums

enum class Tenants(val text: String) {
    UNKNOWN("UNKNOWN"),
    DEFAULT("DEFAULT"),
    MANGA_EXTRACTOR("MANGA_EXTRACTOR"),
    TEXTO_INGLES("TEXTO_INGLES"),
    DECKSUBTITLE("DECKSUBTITLE"),
    TEXTO_JAPONES("TEXTO_JAPONES");
}