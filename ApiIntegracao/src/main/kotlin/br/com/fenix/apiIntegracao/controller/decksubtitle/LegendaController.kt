package br.com.fenix.apiintegracao.controller.decksubtitle

import br.com.fenix.apiintegracao.component.DynamicJdbcRegistry
import br.com.fenix.apiintegracao.controller.ControllerJdbcBase
import br.com.fenix.apiintegracao.controller.Endpoints.Companion.DECK_SUBTITLE
import br.com.fenix.apiintegracao.dto.decsubtitle.LegendaDto
import br.com.fenix.apiintegracao.dto.textojapones.VocabularioDto
import br.com.fenix.apiintegracao.model.decksubtitle.Legenda
import br.com.fenix.apiintegracao.model.textojapones.VocabularioJapones
import br.com.fenix.apiintegracao.repository.decksubtitle.DeckSubtitleRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(DECK_SUBTITLE)
@Tag(name = "Legenda", description = "Endpoint de legendas")
class LegendaController(registry: DynamicJdbcRegistry) : ControllerJdbcBase<UUID?, Legenda, LegendaDto, LegendaController>(DeckSubtitleRepository(registry)) {

}