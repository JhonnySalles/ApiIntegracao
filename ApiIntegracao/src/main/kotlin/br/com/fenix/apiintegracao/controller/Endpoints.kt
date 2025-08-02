package br.com.fenix.apiintegracao.controller

class Endpoints {
    companion object {
        const val API = "/api"

        const val ATUALIZACAO = "atualizacao"
        const val ATUALIZACAO_URL = "/$ATUALIZACAO/{updateDate}"

        const val TEXTO_JAPONES = "$API/texto-japones"
        const val TEXTO_JAPONES_ESTATISTICA = "$TEXTO_JAPONES/estatistica"
        const val TEXTO_JAPONES_EXCLUSAO    = "$TEXTO_JAPONES/exclusao"
        const val TEXTO_JAPONES_FILA_SQL    = "$TEXTO_JAPONES/fila-sql"
        const val TEXTO_JAPONES_KANJAX_PT   = "$TEXTO_JAPONES/kanjax-pt"
        const val TEXTO_JAPONES_KANJI_INFO  = "$TEXTO_JAPONES/kanji-info"
        const val TEXTO_JAPONES_REVISAR     = "$TEXTO_JAPONES/revisar"
        const val TEXTO_JAPONES_VOCABULARIO = "$TEXTO_JAPONES/vocabulario"

        const val TEXTO_INGLES = "$API/texto-ingles"
        const val TEXTO_INGLES_REVISAR  = "$TEXTO_INGLES/revisar"
        const val TEXTO_INGLES_VALIDO   = "$TEXTO_INGLES/valido"
        const val TEXTO_INGLES_EXCLUSAO   = "$TEXTO_INGLES/exclusao"
        const val TEXTO_INGLES_VOCABULARIO   = "$TEXTO_INGLES/vocabulario"

        const val TABLES_URL = "/tabela/{table}"

        const val MANGA_EXTRACTOR = "$API/manga-extractor"
        const val MANGA_EXTRACTOR_CAPITULO = "$MANGA_EXTRACTOR/capitulo"
        const val MANGA_EXTRACTOR_PAGINA = "$MANGA_EXTRACTOR/pagina"
        const val MANGA_EXTRACTOR_TEXTO = "$MANGA_EXTRACTOR/texto"

        const val NOVEL_EXTRACTOR = "$API/novel-extractor"
        const val NOVEL_EXTRACTOR_CAPITULO = "$NOVEL_EXTRACTOR/capitulo"
        const val NOVEL_EXTRACTOR_TEXTO = "$NOVEL_EXTRACTOR/texto"

        const val DECK_SUBTITLE = "$API/deck-subtitle"
        const val COMIC_INFO = "$API/comic-info"
    }
}