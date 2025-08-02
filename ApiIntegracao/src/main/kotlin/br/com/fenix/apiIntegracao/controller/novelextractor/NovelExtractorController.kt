package br.com.fenix.apiintegracao.controller.novelextractor

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry
import br.com.fenix.apiintegracao.controller.ControllerJdbcBaseTabela
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.DECK_SUBTITLE
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.NOVEL_EXTRACTOR
import br.com.fenix.apiintegracao.dto.decsubtitle.LegendaDto
import br.com.fenix.apiintegracao.model.decksubtitle.Legenda
import br.com.fenix.apiintegracao.repository.decksubtitle.DeckSubtitleRepository
import br.com.fenix.apiintegracao.repository.novelextractor.NovelExtractorRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(NOVEL_EXTRACTOR)
@Tag(name = "Novel Extractor", description = "Endpoint para base novel extractor")
class NovelExtractorController(registry: DynamicJdbcRegistry) : ControllerJdbcBaseTabela<UUID?, Legenda, LegendaDto, NovelExtractorController>(NovelExtractorRepository(registry)) {

}