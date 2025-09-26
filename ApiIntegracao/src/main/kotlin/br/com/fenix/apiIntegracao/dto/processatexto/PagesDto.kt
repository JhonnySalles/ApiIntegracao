package br.com.fenix.apiintegracao.dto.processatexto

import br.com.fenix.apiintegracao.enums.comicinfo.ComicPageType
import br.com.fenix.apiintegracao.views.Views
import com.fasterxml.jackson.annotation.JsonView

data class PagesDto(
    @JsonView(Views.Detail::class)
    var bookmark: String? = null,
    @JsonView(Views.Detail::class)
    var image: Int? = null,
    @JsonView(Views.Detail::class)
    var imageHeight: Int? = null,
    @JsonView(Views.Detail::class)
    var imageWidth: Int? = null,
    @JsonView(Views.Detail::class)
    var imageSize: Long? = null,
    @JsonView(Views.Detail::class)
    var type: ComicPageType? = null,
    @JsonView(Views.Detail::class)
    var doublePage: Boolean? = null,
    @JsonView(Views.Detail::class)
    var key: String? = null
) { }