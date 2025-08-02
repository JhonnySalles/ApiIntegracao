package br.com.fenix.apiintegracao.controller.mangaextractor

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry
import br.com.fenix.apiintegracao.controller.ControllerJdbcBaseTabela
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.DECK_SUBTITLE
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.MANGA_EXTRACTOR
import br.com.fenix.apiintegracao.dto.decsubtitle.LegendaDto
import br.com.fenix.apiintegracao.dto.mangaextractor.MangaVolumeDto
import br.com.fenix.apiintegracao.enums.comicinfo.Manga
import br.com.fenix.apiintegracao.model.decksubtitle.Legenda
import br.com.fenix.apiintegracao.model.mangaextractor.MangaVolume
import br.com.fenix.apiintegracao.repository.decksubtitle.DeckSubtitleRepository
import br.com.fenix.apiintegracao.repository.mangaextractor.MangaExtractorRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(MANGA_EXTRACTOR)
@Tag(name = "Manga Extractor", description = "Endpoint para base manga extractor")
class MangaExtractorController(registry: DynamicJdbcRegistry) : ControllerJdbcBaseTabela<UUID?, MangaVolume, MangaVolumeDto, MangaExtractorController>(MangaExtractorRepository(registry), MangaVolume.Companion) {

}