package br.com.fenix.apiIntegracao.controller

class Endpoints {
    companion object {
        const val API = "/api"

        const val ATUALIZACAO_URL = "/atualizacao/{updateDate}"

        const val TEXTO_JAPONES = "$API/texto-japones"
        const val TEXTO_JAPONES_ESTATISTICA = "$TEXTO_JAPONES/estatistica"
        const val TEXTO_JAPONES_EXCLUSAO    = "$TEXTO_JAPONES/exclusao"
        const val TEXTO_JAPONES_FILA_SQL    = "$TEXTO_JAPONES/fila-sql"
        const val TEXTO_JAPONES_KANJAX_PT   = "$TEXTO_JAPONES/kanjax-pt"
        const val TEXTO_JAPONES_KANJI_INFO  = "$TEXTO_JAPONES/kanji-info"
        const val TEXTO_JAPONES_REVISAR     = "$TEXTO_JAPONES/revisar"

        const val TEXTO_JAPONES_VOCABULARIO = "$TEXTO_JAPONES/vocabulario"
        const val TEXTO_INGLES = "$API/texto-ingles"
        const val TEXTO_INGLES_REVISAR     = "$TEXTO_INGLES/revisar"
        const val TEXTO_INGLES_VALIDO     = "$TEXTO_INGLES/valido"

        const val TEXTO_INGLES_VOCABULARIO = "$TEXTO_INGLES/vocabulario"
        const val TABLES_URL = "/tabela/{tables}/"

        const val MANGA_EXTRACTOR = "$API/manga-extractor"
        const val DECK_SUBTITLE = "$API/deck-subtitle"
    }
}