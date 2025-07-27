package br.com.fenix.apiintegracao.dto.processatexto

import br.com.fenix.apiintegracao.enums.comicinfo.ComicPageType

data class PagesDto(
    var bookmark: String? = null,
    var image: Int? = null,
    var imageHeight: Int? = null,
    var imageWidth: Int? = null,
    var imageSize: Long? = null,
    var type: ComicPageType? = null,
    var doublePage: Boolean? = null,
    var key: String? = null
) { }