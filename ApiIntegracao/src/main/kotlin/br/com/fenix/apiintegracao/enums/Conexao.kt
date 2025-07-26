package br.com.fenix.apiintegracao.enums

//As pastas de model e repository devem estar com o mesmo nome definido no packages
enum class Conexao(private val value: String, val packages : String) {
    API("API", "api"),
    MANGA_EXTRACTOR("MANGA_EXTRACTOR", "mangaextractor"),
    NOVEL_EXTRACTOR("NOVEL_EXTRACTOR", "novelextractor"),
    TEXTO_INGLES("TEXTO_INGLES", "textoingles"),
    TEXTO_JAPONES("TEXTO_JAPONES", "textojapones"),
    DECKSUBTITLE("DECKSUBTITLE", "decksubtitle"),
    FIREBASE("FIREBASE", "firebase");

    @Override
    override fun toString(): String {
        return value
    }
}