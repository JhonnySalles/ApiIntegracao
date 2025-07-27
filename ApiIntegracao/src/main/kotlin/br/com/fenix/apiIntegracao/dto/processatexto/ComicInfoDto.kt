package br.com.fenix.apiintegracao.dto.processatexto

import br.com.fenix.apiintegracao.dto.DtoBase
import br.com.fenix.apiintegracao.enums.comicinfo.AgeRating
import br.com.fenix.apiintegracao.enums.comicinfo.Manga
import br.com.fenix.apiintegracao.enums.comicinfo.YesNo
import java.util.*

data class ComicInfoDto(
    private var id: UUID? = null,
    var idMal: Long? = null,
    var comic: String = "",
    var title: String = "",
    var series: String = "",
    var number: Float = 0f,
    var volume: Int = 0,
    var notes: String? = null,
    var year: Int? = null,
    var month: Int? = null,
    var day: Int? = null,
    var writer: String? = null,
    var penciller: String? = null,
    var inker: String? = null,
    var coverArtist: String? = null,
    var colorist: String? = null,
    var letterer: String? = null,
    var publisher: String? = null,
    var tags: String? = null,
    var web: String? = null,
    var editor: String? = null,
    var translator: String? = null,
    var pageCount: Int? = null,
    var pages: List<PagesDto>? = null,
    var count: Int? = null,
    var alternateSeries: String? = null,
    var alternateNumber: Float? = null,
    var storyArc: String? = null,
    var storyArcNumber: String? = null,
    var seriesGroup: String? = null,
    var alternateCount: Int? = null,
    var summary: String? = null,
    var imprint: String? = null,
    var genre: String? = null,
    var languageISO: String = "",
    var format: String? = null,
    var ageRating: AgeRating? = null,
    var communityRating: Float? = null,
    var blackAndWhite: YesNo? = null,
    var manga: Manga = Manga.Yes,
    var characters: String? = null,
    var teams: String? = null,
    var locations: String? = null,
    var scanInformation: String? = null,
    var mainCharacterOrTeam: String? = null,
    var review: String? = null,
) : DtoBase<UUID?>() {

    override fun getId(): UUID? = id

    override fun setId(id: UUID?) {
        this.id = id
    }

}